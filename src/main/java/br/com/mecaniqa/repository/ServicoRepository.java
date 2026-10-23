package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Servico;
import br.com.mecaniqa.mapper.ServicoResultSetMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoRepository
        extends ConsultaJdbcTemplate
        implements Repository<Servico, Long> {

    private static ServicoRepository instance;

    private ServicoRepository() {
    }

    public static ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }

        return instance;
    }

    @Override
    public Servico save(Servico servico) {
        String sql = """
                INSERT INTO servicos (
                    descricao,
                    valor_mao_de_obra,
                    custo_tabelado,
                    tempo_estimado_minutos,
                    data_cadastro,
                    data_atualizacao
                ) VALUES (?, ?, ?, ?, NOW(), NOW())
                """;

        long id = executarInsercao(
                sql,
                statement -> preencherDados(statement, servico)
        );

        servico.setId(id);
        return findById(id).orElseThrow(
                () -> new IllegalStateException("O servico inserido nao foi encontrado."));
    }

    @Override
    public Optional<Servico> findById(Long id) {
        String sql = "SELECT * FROM servicos WHERE id = ?";

        return executarConsulta(
                sql,
                new Consulta<Optional<Servico>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement)
                            throws SQLException {

                        statement.setLong(1, id);
                    }

                    @Override
                    public Optional<Servico> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        if (resultado.next()) {
                            return Optional.of(
                                    ServicoResultSetMapper.toModel(resultado)
                            );
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<Servico> findAll() {
        String sql = "SELECT * FROM servicos ORDER BY id";

        return executarConsulta(
                sql,
                new Consulta<List<Servico>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement) {
                    }

                    @Override
                    public List<Servico> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        List<Servico> servicos = new ArrayList<>();

                        while (resultado.next()) {
                            servicos.add(
                                    ServicoResultSetMapper.toModel(resultado)
                            );
                        }

                        return servicos;
                    }
                }
        );
    }

    @Override
    public Servico update(Servico servico) {
        String sql = """
                UPDATE servicos
                SET descricao = ?,
                    valor_mao_de_obra = ?,
                    custo_tabelado = ?,
                    tempo_estimado_minutos = ?,
                    data_atualizacao = NOW()
                WHERE id = ?
                """;

        int linhas = executarAtualizacao(
                sql,
                statement -> {
                    preencherDados(statement, servico);
                    statement.setLong(5, servico.getId());
                }
        );

        if (linhas == 0) {
            throw new IllegalStateException("O servico a atualizar nao existe.");
        }
        return findById(servico.getId()).orElseThrow(
                () -> new IllegalStateException("O servico atualizado nao foi encontrado."));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM servicos WHERE id = ?";

        executarAtualizacao(
                sql,
                statement -> statement.setLong(1, id)
        );
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM servicos
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

    public Servico salvar(Servico servico) {
        return save(servico);
    }

    public List<Servico> listar() {
        return findAll();
    }

    public Optional<Servico> buscarPorId(Long id) {
        return findById(id);
    }

    public Optional<Servico> atualizar(Long id, Servico novosDados) {
        if (!existsById(id)) {
            return Optional.empty();
        }
        novosDados.setId(id);
        return Optional.of(update(novosDados));
    }

    public boolean excluir(Long id) {
        if (!existsById(id)) {
            return false;
        }
        deleteById(id);
        return true;
    }

    private void preencherDados(
            PreparedStatement statement,
            Servico servico)
            throws SQLException {

        statement.setString(1, servico.getDescricao());

        statement.setBigDecimal(
                2,
                servico.getValorMaoDeObra() == null
                        ? null
                        : BigDecimal.valueOf(servico.getValorMaoDeObra())
        );

        statement.setBigDecimal(
                3,
                servico.getCustoTabelado() == null
                        ? null
                        : BigDecimal.valueOf(servico.getCustoTabelado())
        );

        statement.setBigDecimal(
                4,
                servico.getTempoEstimadoMinutos() == null
                        ? null
                        : BigDecimal.valueOf(servico.getTempoEstimadoMinutos())
        );
    }

}
