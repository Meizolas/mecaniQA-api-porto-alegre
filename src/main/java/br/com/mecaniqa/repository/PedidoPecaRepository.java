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


public class PedidoPecaRepository
        extends ConsultaJdbcTemplate
        implements Repository<PedidoPeca, Long> {

    private static PedidoPecaRepository instance;

    protected PedidoPecaRepository() {
    }

    public static PedidoPecaRepository getInstance() {
        if (instance == null) {
            instance = new PedidoPecaRepository();
        }

        return instance;
    }


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

        return executarTransacao(conexao -> {
            long id = executarInsercao(
                    conexao,
                    sql,
                    statement -> preencherDados(statement, pedido)
            );
            pedido.setId(id);

            for (ItemPedidoPeca item : pedido.getItens()) {
                inserirItem(conexao, id, item);
            }
            return pedido;
        });
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

    @Override
    public PedidoPeca update(PedidoPeca pedido) {
        String sql = """
                UPDATE pedidos_pecas
                SET status = ?,
                    data_ultima_atualizacao = ?
                WHERE id = ?
                """;

        pedido.setDataUltimaAtualizacao(LocalDateTime.now());

        executarAtualizacao(
                sql,
                statement -> {
                    statement.setString(1, pedido.getStatus().name());
                    statement.setTimestamp(
                            2,
                            Timestamp.valueOf(pedido.getDataUltimaAtualizacao())
                    );
                    statement.setLong(3, pedido.getId());
                }
        );

        return pedido;
    }

    @Override
    public void deleteById(Long id) {
        // A chave estrangeira dos itens possui ON DELETE CASCADE.
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

        boolean atualizado = executarTransacao(conexao -> {
            int existe = executarAtualizacao(
                    conexao,
                    """
                    UPDATE pedidos_pecas
                    SET data_ultima_atualizacao = ?
                    WHERE id = ?
                    """,
                    statement -> {
                        statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                        statement.setLong(2, id);
                    });
            if (existe == 0) {
                return false;
            }
            for (ItemPedidoPeca item : itens) {
                inserirItem(conexao, id, item);
            }
            return true;
        });

        if (!atualizado) {
            return Optional.empty();
        }

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
    private void inserirItem(
            java.sql.Connection conexao,
            Long pedidoId,
            ItemPedidoPeca item) throws SQLException {
        String sql = """
                INSERT INTO itens_pedido_peca (
                    pedido_id,
                    codigo_peca,
                    quantidade,
                    preco_unitario
                ) VALUES (?, ?, ?, ?)
                """;

        long idItem = executarInsercao(
                conexao,
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
