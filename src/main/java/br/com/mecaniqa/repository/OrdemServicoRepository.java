package br.com.mecaniqa.repository;

import br.com.mecaniqa.mapper.OrdemServicoResultSetMapper;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdemServicoRepository
        extends ConsultaJdbcTemplate
        implements Repository<OrdemServico, Long> {

    private static OrdemServicoRepository instance;

    private OrdemServicoRepository() {
    }

    public static OrdemServicoRepository getInstance() {
        if (instance == null) {
            instance = new OrdemServicoRepository();
        }

        return instance;
    }

    @Override
    public OrdemServico save(OrdemServico ordem) {
        String sql = """
                INSERT INTO ordens_servico (
                    descricao_problema,
                    data_abertura,
                    status,
                    valor
                ) VALUES (?, ?, ?, ?)
                """;

        long id = executarInsercao(
                sql,
                statement -> preencherDados(statement, ordem)
        );

        ordem.setId(id);
        return ordem;
    }

    @Override
    public Optional<OrdemServico> findById(Long id) {
        String sql = "SELECT * FROM ordens_servico WHERE id = ?";

        return executarConsulta(
                sql,
                new Consulta<Optional<OrdemServico>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement)
                            throws SQLException {

                        statement.setLong(1, id);
                    }

                    @Override
                    public Optional<OrdemServico> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        if (resultado.next()) {
                            return Optional.of(
                                    OrdemServicoResultSetMapper
                                            .toModel(resultado)
                            );
                        }

                        return Optional.empty();
                    }
                }
        );
    }

    @Override
    public List<OrdemServico> findAll() {
        String sql = "SELECT * FROM ordens_servico ORDER BY id";

        return executarConsulta(
                sql,
                new Consulta<List<OrdemServico>>() {

                    @Override
                    public void preencherParametros(
                            PreparedStatement statement) {
                        // Consulta sem parâmetros.
                    }

                    @Override
                    public List<OrdemServico> mapearResultado(
                            ResultSet resultado)
                            throws SQLException {

                        List<OrdemServico> ordens = new ArrayList<>();

                        while (resultado.next()) {
                            ordens.add(
                                    OrdemServicoResultSetMapper
                                            .toModel(resultado)
                            );
                        }

                        return ordens;
                    }
                }
        );
    }

    @Override
    public OrdemServico update(OrdemServico ordem) {
        String sql = """
                UPDATE ordens_servico
                SET descricao_problema = ?,
                    valor = ?
                WHERE id = ?
                """;

        executarAtualizacao(
                sql,
                statement -> {
                    statement.setString(1, ordem.getDescricaoProblema());
                    statement.setBigDecimal(2, BigDecimal.valueOf(ordem.getValor()));
                    statement.setLong(3, ordem.getId());
                }
        );

        return ordem;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM ordens_servico WHERE id = ?";

        executarAtualizacao(
                sql,
                statement -> statement.setLong(1, id)
        );
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM ordens_servico
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

    public OrdemServico salvar(OrdemServico ordem) {
        return save(ordem);
    }

    public List<OrdemServico> listar() {
        return findAll();
    }

    public Optional<OrdemServico> buscarPorId(Long id) {
        return findById(id);
    }

    public Optional<OrdemServico> atualizarStatus(
            Long id,
            StatusOrdemServico status) {

        String sql = """
                UPDATE ordens_servico
                SET status = ?
                WHERE id = ?
                """;

        executarAtualizacao(
                sql,
                statement -> {
                    statement.setString(1, status.name());
                    statement.setLong(2, id);
                }
        );

        return findById(id);
    }

    private void preencherDados(
            PreparedStatement statement,
            OrdemServico ordem)
            throws SQLException {

        statement.setString(1, ordem.getDescricaoProblema());

        statement.setDate(
                2,
                Date.valueOf(ordem.getDataAbertura())
        );

        statement.setString(3, ordem.getStatus().name());

        statement.setBigDecimal(
                4,
                BigDecimal.valueOf(ordem.getValor())
        );
    }
}
