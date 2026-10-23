package br.com.mecaniqa.repository;

import br.com.mecaniqa.config.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConsultaJdbcTemplate {

    protected Connection abrirConexao() throws SQLException {
        return Conexao.getConnection();
    }

    protected final long executarInsercao(String sql, Parametros parametros) {
        try (Connection conexao = abrirConexao()) {
            return executarInsercao(conexao, sql, parametros);
        } catch (SQLException e) {
            throw falha("Erro ao inserir registro no banco de dados.", e);
        }
    }

    protected final long executarInsercao(
            Connection conexao,
            String sql,
            Parametros parametros) throws SQLException {

        try (PreparedStatement statement = conexao.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros.preencher(statement);
            if (statement.executeUpdate() != 1) {
                throw new SQLException("A insercao nao criou o registro esperado.");
            }

            try (ResultSet chaves = statement.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getLong(1);
                }
                throw new SQLException("O banco nao retornou o identificador gerado.");
            }
        }
    }

    protected final <R> R executarConsulta(String sql, Consulta<R> consulta) {
        try (Connection conexao = abrirConexao();
             PreparedStatement statement = conexao.prepareStatement(sql)) {
            consulta.preencherParametros(statement);
            try (ResultSet resultado = statement.executeQuery()) {
                return consulta.mapearResultado(resultado);
            }
        } catch (SQLException e) {
            throw falha("Erro ao executar consulta no banco de dados.", e);
        }
    }

    protected final int executarAtualizacao(String sql, Parametros parametros) {
        try (Connection conexao = abrirConexao()) {
            return executarAtualizacao(conexao, sql, parametros);
        } catch (SQLException e) {
            throw falha("Erro ao executar alteracao no banco de dados.", e);
        }
    }

    protected final int executarAtualizacao(
            Connection conexao,
            String sql,
            Parametros parametros) throws SQLException {
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            parametros.preencher(statement);
            return statement.executeUpdate();
        }
    }

    protected final <R> R executarTransacao(Transacao<R> transacao) {
        try (Connection conexao = abrirConexao()) {
            boolean autoCommitOriginal = conexao.getAutoCommit();
            conexao.setAutoCommit(false);
            try {
                R resultado = transacao.executar(conexao);
                conexao.commit();
                return resultado;
            } catch (Exception e) {
                try {
                    conexao.rollback();
                } catch (SQLException falhaRollback) {
                    e.addSuppressed(falhaRollback);
                }
                if (e instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw falha("Erro ao executar transacao no banco de dados.", e);
            } finally {
                try {
                    conexao.setAutoCommit(autoCommitOriginal);
                } catch (SQLException ignored) {
                    // O try-with-resources ainda fechara a conexao.
                }
            }
        } catch (SQLException e) {
            throw falha("Erro ao executar transacao no banco de dados.", e);
        }
    }

    private RuntimeException falha(String mensagem, Exception causa) {
        return new RuntimeException(mensagem, causa);
    }

    protected interface Consulta<R> {
        void preencherParametros(PreparedStatement statement) throws SQLException;
        R mapearResultado(ResultSet resultado) throws SQLException;
    }

    @FunctionalInterface
    protected interface Parametros {
        void preencher(PreparedStatement statement) throws SQLException;
    }

    @FunctionalInterface
    protected interface Transacao<R> {
        R executar(Connection conexao) throws Exception;
    }
}
