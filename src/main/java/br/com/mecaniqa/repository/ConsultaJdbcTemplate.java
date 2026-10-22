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

    protected final long executarInsercao(
        String sql,
        Parametros parametros) {

    try (
            Connection conexao = abrirConexao();
            PreparedStatement statement = conexao.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            )
    ) {
        parametros.preencher(statement);

        int linhas = statement.executeUpdate();

        if (linhas != 1) {
            throw new SQLException(
                    "A inserção não criou o registro esperado."
            );
        }

        try (ResultSet chaves = statement.getGeneratedKeys()) {
            if (chaves.next()) {
                return chaves.getLong(1);
            }

            throw new SQLException(
                    "O banco não retornou o identificador gerado."
            );
        }

    } catch (SQLException e) {
        throw new RuntimeException(
                "Erro ao inserir registro no banco de dados.",
                e
        );
    }
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