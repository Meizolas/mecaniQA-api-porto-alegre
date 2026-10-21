package br.com.mecaniqa.repository;

import br.com.mecaniqa.config.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ConsultaJdbcTemplate {

    protected Connection abrirConexao() throws SQLException {
        return Conexao.getConnection();
    }

    protected final <R> R executarConsulta(
            String sql,
            Consulta<R> consulta) {

        try (
                Connection conexao = abrirConexao();
                PreparedStatement statement =
                        conexao.prepareStatement(sql)
        ) {
            consulta.preencherParametros(statement);

            try (ResultSet resultado = statement.executeQuery()) {
                return consulta.mapearResultado(resultado);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao executar consulta no banco de dados.",
                    e
            );
        }
    }

    protected final int executarAtualizacao(
            String sql,
            Parametros parametros) {

        try (
                Connection conexao = abrirConexao();
                PreparedStatement statement =
                        conexao.prepareStatement(sql)
        ) {
            parametros.preencher(statement);

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao executar alteração no banco de dados.",
                    e
            );
        }
    }

    protected interface Consulta<R> {

        void preencherParametros(PreparedStatement statement)
                throws SQLException;

        R mapearResultado(ResultSet resultado)
                throws SQLException;
    }

    protected interface Parametros {

        void preencher(PreparedStatement statement)
                throws SQLException;
    }
}