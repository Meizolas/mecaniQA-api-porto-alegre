package br.com.mecaniqa.repository;

import br.com.mecaniqa.mapper.PecaResultSetMapper;
import br.com.mecaniqa.model.Peca;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PecaRepository
        extends ConsultaJdbcTemplate
        implements Repository<Peca, Long> {

    private static PecaRepository instance;

    private PecaRepository() {
    }

    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }
        return instance;
    }

    @Override
    public Peca save(Peca peca) {
        String sql = """
                INSERT INTO pecas (
                    codigo_barras, fornecedor_marca, quantidade_estoque,
                    preco_custo, preco_venda, tamanho, cor, categoria,
                    data_cadastro, data_ultima_atualizacao
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;
        long codigo = executarInsercao(sql, statement -> preencherDados(statement, peca));
        peca.setCodigo(codigo);
        return findById(codigo).orElseThrow(
                () -> new IllegalStateException("A peca inserida nao foi encontrada."));
    }

    @Override
    public Optional<Peca> findById(Long codigo) {
        return executarConsulta(
                "SELECT * FROM pecas WHERE codigo = ?",
                new Consulta<Optional<Peca>>() {
                    @Override
                    public void preencherParametros(PreparedStatement statement)
                            throws SQLException {
                        statement.setLong(1, codigo);
                    }

                    @Override
                    public Optional<Peca> mapearResultado(ResultSet resultado)
                            throws SQLException {
                        return resultado.next()
                                ? Optional.of(PecaResultSetMapper.toModel(resultado))
                                : Optional.empty();
                    }
                });
    }

    @Override
    public List<Peca> findAll() {
        return executarConsulta(
                "SELECT * FROM pecas ORDER BY codigo",
                new Consulta<List<Peca>>() {
                    @Override
                    public void preencherParametros(PreparedStatement statement) {
                    }

                    @Override
                    public List<Peca> mapearResultado(ResultSet resultado)
                            throws SQLException {
                        List<Peca> pecas = new ArrayList<>();
                        while (resultado.next()) {
                            pecas.add(PecaResultSetMapper.toModel(resultado));
                        }
                        return pecas;
                    }
                });
    }

    @Override
    public Peca update(Peca peca) {
        String sql = """
                UPDATE pecas
                SET codigo_barras = ?, fornecedor_marca = ?, quantidade_estoque = ?,
                    preco_custo = ?, preco_venda = ?, tamanho = ?, cor = ?,
                    categoria = ?, data_ultima_atualizacao = NOW()
                WHERE codigo = ?
                """;
        int linhas = executarAtualizacao(sql, statement -> {
            preencherDados(statement, peca);
            statement.setLong(9, peca.getCodigo());
        });
        if (linhas == 0) {
            throw new IllegalStateException("A peca a atualizar nao existe.");
        }
        return findById(peca.getCodigo()).orElseThrow(
                () -> new IllegalStateException("A peca atualizada nao foi encontrada."));
    }

    @Override
    public void deleteById(Long codigo) {
        executarAtualizacao(
                "DELETE FROM pecas WHERE codigo = ?",
                statement -> statement.setLong(1, codigo));
    }

    @Override
    public boolean existsById(Long codigo) {
        return executarConsulta(
                "SELECT COUNT(*) FROM pecas WHERE codigo = ?",
                new Consulta<Boolean>() {
                    @Override
                    public void preencherParametros(PreparedStatement statement)
                            throws SQLException {
                        statement.setLong(1, codigo);
                    }

                    @Override
                    public Boolean mapearResultado(ResultSet resultado)
                            throws SQLException {
                        resultado.next();
                        return resultado.getInt(1) > 0;
                    }
                });
    }

    public int contar() {
        return executarConsulta(
                "SELECT COUNT(*) FROM pecas",
                new Consulta<Integer>() {
                    @Override
                    public void preencherParametros(PreparedStatement statement) {
                    }

                    @Override
                    public Integer mapearResultado(ResultSet resultado)
                            throws SQLException {
                        resultado.next();
                        return resultado.getInt(1);
                    }
                });
    }

    public Peca salvar(Peca peca) {
        return save(peca);
    }

    public List<Peca> listar() {
        return findAll();
    }

    public Optional<Peca> buscarPorCodigo(Long codigo) {
        return findById(codigo);
    }

    public Optional<Peca> atualizar(Long codigo, Peca novosDados) {
        if (!existsById(codigo)) {
            return Optional.empty();
        }
        novosDados.setCodigo(codigo);
        return Optional.of(update(novosDados));
    }

    public boolean excluir(Long codigo) {
        if (!existsById(codigo)) {
            return false;
        }
        deleteById(codigo);
        return true;
    }

    private void preencherDados(PreparedStatement statement, Peca peca)
            throws SQLException {
        statement.setString(1, peca.getCodigoBarras());
        statement.setString(2, peca.getFornecedorMarca());
        if (peca.getQuantidadeEstoque() == null) {
            statement.setNull(3, Types.INTEGER);
        } else {
            statement.setInt(3, peca.getQuantidadeEstoque());
        }
        preencherDecimal(statement, 4, peca.getPrecoCusto());
        preencherDecimal(statement, 5, peca.getPrecoVenda());
        statement.setString(6, peca.getTamanho());
        statement.setString(7, peca.getCor());
        if (peca.getCategoria() == null) {
            statement.setNull(8, Types.VARCHAR);
        } else {
            statement.setString(8, peca.getCategoria().name());
        }
    }

    private void preencherDecimal(PreparedStatement statement, int indice, Double valor)
            throws SQLException {
        if (valor == null) {
            statement.setNull(indice, Types.DECIMAL);
        } else {
            statement.setBigDecimal(indice, BigDecimal.valueOf(valor));
        }
    }
}
