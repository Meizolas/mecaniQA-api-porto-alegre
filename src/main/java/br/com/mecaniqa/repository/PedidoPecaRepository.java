package br.com.mecaniqa.repository;

import br.com.mecaniqa.mapper.PedidoPecaResultSetMapper;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório JDBC de pedidos de peças.
 *
 * Usa o ConsultaJdbcTemplate (Template Method) para abrir/fechar
 * conexão, statement e result set, e implementa a interface
 * genérica Repository. Um pedido é gravado em duas tabelas:
 * pedidos_pecas (cabeçalho) e itens_pedido_peca (entidade associativa
 * entre pedido e peça).
 */
public class PedidoPecaRepository
        extends ConsultaJdbcTemplate
        implements Repository<PedidoPeca, Long> {

    private static PedidoPecaRepository instance;

    private PedidoPecaRepository() {
    }

    public static PedidoPecaRepository getInstance() {
        if (instance == null) {
            instance = new PedidoPecaRepository();
        }

        return instance;
    }

    // ---------------------------------------------------------------
    // Métodos da interface Repository
    // ---------------------------------------------------------------

    @Override
    public PedidoPeca save(PedidoPeca pedido) {
        String sql = """
                INSERT INTO pedidos_pecas (
                    status,
                    data_criacao,
                    data_ultima_atualizacao
                ) VALUES (?, ?, ?)
                """;

        LocalDateTime agora = LocalDateTime.now();
        pedido.setDataCriacao(agora);
        pedido.setDataUltimaAtualizacao(agora);

        if (pedido.getStatus() == null) {
            pedido.setStatus(StatusPedidoPeca.ORCANDO);
        }

        long id = executarInsercao(
                sql,
                statement -> preencherDados(statement, pedido)
        );

        pedido.setId(id);

        for (ItemPedidoPeca item : pedido.getItens()) {
            inserirItem(id, item);
        }

        return pedido;
    }

    @Override
    public Optional<PedidoPeca> findById(Long id) {
        String sql = "SELECT * FROM pedidos_pecas WHERE id = ?";

        Optional<PedidoPeca> pedido = executarConsulta(
                sql,
                new Consulta<Optional<PedidoPeca>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement)
                            throws SQLException {

                        statement.setLong(1, id);
                    }

                    @Override
                    public Optional<PedidoPeca> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        if (resultado.next()) {
                            return Optional.of(
                                    PedidoPecaResultSetMapper
                                            .toModel(resultado)
                            );
                        }

                        return Optional.empty();
                    }
                }
        );

        pedido.ifPresent(p -> p.setItens(buscarItens(p.getId())));

        return pedido;
    }

    @Override
    public List<PedidoPeca> findAll() {
        String sql = "SELECT * FROM pedidos_pecas ORDER BY id";

        List<PedidoPeca> pedidos = executarConsulta(
                sql,
                new Consulta<List<PedidoPeca>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement) {
                        // Consulta sem parâmetros.
                    }

                    @Override
                    public List<PedidoPeca> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        List<PedidoPeca> lista = new ArrayList<>();

                        while (resultado.next()) {
                            lista.add(
                                    PedidoPecaResultSetMapper
                                            .toModel(resultado)
                            );
                        }

                        return lista;
                    }
                }
        );

        for (PedidoPeca pedido : pedidos) {
            pedido.setItens(buscarItens(pedido.getId()));
        }

        return pedidos;
    }

    /**
     * Atualiza os dados do cabeçalho do pedido (status e data).
     * Os itens são incluídos pelo método adicionarItens.
     */
    @Override
    public PedidoPeca update(PedidoPeca pedido) {
        String sql = """
                UPDATE pedidos_pecas
                SET status = ?,
                    data_criacao = ?,
                    data_ultima_atualizacao = ?
                WHERE id = ?
                """;

        pedido.setDataUltimaAtualizacao(LocalDateTime.now());

        executarAtualizacao(
                sql,
                statement -> {
                    preencherDados(statement, pedido);
                    statement.setLong(4, pedido.getId());
                }
        );

        return pedido;
    }

    @Override
    public void deleteById(Long id) {
        // Remove primeiro os itens por causa da chave estrangeira.
        executarAtualizacao(
                "DELETE FROM itens_pedido_peca WHERE pedido_id = ?",
                statement -> statement.setLong(1, id)
        );

        executarAtualizacao(
                "DELETE FROM pedidos_pecas WHERE id = ?",
                statement -> statement.setLong(1, id)
        );
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM pedidos_pecas
                WHERE id = ?
                """;

        return executarConsulta(
                sql,
                new Consulta<Boolean>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement)
                            throws SQLException {

                        statement.setLong(1, id);
                    }

                    @Override
                    public Boolean mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        resultado.next();
                        return resultado.getInt(1) > 0;
                    }
                }
        );
    }

    // ---------------------------------------------------------------
    // Métodos usados pelo PedidoPecaController (mantidos para não
    // quebrar a camada de cima)
    // ---------------------------------------------------------------

    public PedidoPeca salvar(PedidoPeca pedido) {
        return save(pedido);
    }

    public List<PedidoPeca> listar() {
        return findAll();
    }

    public Optional<PedidoPeca> buscarPorId(Long id) {
        return findById(id);
    }

    public Optional<PedidoPeca> adicionarItens(
            Long id,
            List<ItemPedidoPeca> itens) {

        if (!existsById(id)) {
            return Optional.empty();
        }

        for (ItemPedidoPeca item : itens) {
            inserirItem(id, item);
        }

        tocarDataAtualizacao(id);

        return findById(id);
    }

    public Optional<PedidoPeca> atualizarStatus(
            Long id,
            StatusPedidoPeca novoStatus) {

        String sql = """
                UPDATE pedidos_pecas
                SET status = ?,
                    data_ultima_atualizacao = ?
                WHERE id = ?
                """;

        int linhas = executarAtualizacao(
                sql,
                statement -> {
                    statement.setString(1, novoStatus.name());
                    statement.setTimestamp(
                            2,
                            Timestamp.valueOf(LocalDateTime.now())
                    );
                    statement.setLong(3, id);
                }
        );

        if (linhas == 0) {
            return Optional.empty();
        }

        return findById(id);
    }

    // ---------------------------------------------------------------
    // Itens do pedido (tabela itens_pedido_peca)
    // ---------------------------------------------------------------

    private void inserirItem(Long pedidoId, ItemPedidoPeca item) {
        String sql = """
                INSERT INTO itens_pedido_peca (
                    pedido_id,
                    codigo_peca,
                    quantidade,
                    preco_unitario
                ) VALUES (?, ?, ?, ?)
                """;

        long idItem = executarInsercao(
                sql,
                statement -> {
                    statement.setLong(1, pedidoId);
                    statement.setLong(2, item.getPeca().getCodigo());
                    statement.setInt(3, item.getQuantidade());

                    if (item.getPrecoUnitario() == null) {
                        statement.setNull(4, Types.DECIMAL);
                    } else {
                        statement.setBigDecimal(
                                4,
                                BigDecimal.valueOf(item.getPrecoUnitario())
                        );
                    }
                }
        );

        item.setId(idItem);
    }

    private List<ItemPedidoPeca> buscarItens(Long pedidoId) {
        // O JOIN traz as colunas da peça junto com as do item,
        // assim o PecaResultSetMapper consegue montar a Peca.
        String sql = """
                SELECT i.id AS item_id,
                       i.quantidade,
                       i.preco_unitario,
                       p.*
                FROM itens_pedido_peca i
                JOIN pecas p ON p.codigo = i.codigo_peca
                WHERE i.pedido_id = ?
                ORDER BY i.id
                """;

        return executarConsulta(
                sql,
                new Consulta<List<ItemPedidoPeca>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement)
                            throws SQLException {

                        statement.setLong(1, pedidoId);
                    }

                    @Override
                    public List<ItemPedidoPeca> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        List<ItemPedidoPeca> itens = new ArrayList<>();

                        while (resultado.next()) {
                            itens.add(
                                    PedidoPecaResultSetMapper
                                            .toItemModel(resultado)
                            );
                        }

                        return itens;
                    }
                }
        );
    }

    private void tocarDataAtualizacao(Long id) {
        executarAtualizacao(
                """
                UPDATE pedidos_pecas
                SET data_ultima_atualizacao = ?
                WHERE id = ?
                """,
                statement -> {
                    statement.setTimestamp(
                            1,
                            Timestamp.valueOf(LocalDateTime.now())
                    );
                    statement.setLong(2, id);
                }
        );
    }

    private void preencherDados(
            PreparedStatement statement,
            PedidoPeca pedido)
            throws SQLException {

        statement.setString(1, pedido.getStatus().name());

        statement.setTimestamp(
                2,
                Timestamp.valueOf(pedido.getDataCriacao())
        );

        statement.setTimestamp(
                3,
                Timestamp.valueOf(pedido.getDataUltimaAtualizacao())
        );
    }
}
