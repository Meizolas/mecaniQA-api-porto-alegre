package br.com.mecaniqa.repository;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ConsultaJdbcTemplateTest {

    private static class ConsultaTeste
            extends ConsultaJdbcTemplate {

        private final Connection conexao;

        ConsultaTeste(Connection conexao) {
            this.conexao = conexao;
        }

        @Override
        protected Connection abrirConexao() {
            return conexao;
        }

        int consultar() {
            return executarConsulta(
                    "SELECT COUNT(*) FROM pecas",
                    new Consulta<Integer>() {

                        @Override
                        public void preencherParametros(
                                PreparedStatement statement) {
                        }

                        @Override
                        public Integer mapearResultado(
                                ResultSet resultado)
                                throws SQLException {

                            resultado.next();
                            return resultado.getInt(1);
                        }
                    }
            );
        }
    }

    @Test
    void deveFecharRecursosQuandoConsultaFunciona()
            throws SQLException {

        Connection conexao = mock(Connection.class);

        PreparedStatement statement =
                mock(PreparedStatement.class);

        ResultSet resultado = mock(ResultSet.class);

        when(conexao.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(true);
        when(resultado.getInt(1)).thenReturn(1);

        ConsultaTeste consulta = new ConsultaTeste(conexao);

        assertEquals(1, consulta.consultar());

        verify(resultado).close();
        verify(statement).close();
        verify(conexao).close();
    }

    @Test
    void deveFecharRecursosQuandoExecucaoFalha()
            throws SQLException {

        Connection conexao = mock(Connection.class);

        PreparedStatement statement =
                mock(PreparedStatement.class);

        when(conexao.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenThrow(new SQLException("Falha simulada"));

        ConsultaTeste consulta = new ConsultaTeste(conexao);

        assertThrows(
                RuntimeException.class,
                consulta::consultar
        );

        verify(statement).close();
        verify(conexao).close();
    }

    @Test
    void deveFecharRecursosQuandoMapeamentoFalha()
            throws SQLException {

        Connection conexao = mock(Connection.class);

        PreparedStatement statement =
                mock(PreparedStatement.class);

        ResultSet resultado = mock(ResultSet.class);

        when(conexao.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(resultado);
        when(resultado.next()).thenReturn(true);

        when(resultado.getInt(1))
                .thenThrow(new SQLException("Falha no mapeamento"));

        ConsultaTeste consulta = new ConsultaTeste(conexao);

        assertThrows(
                RuntimeException.class,
                consulta::consultar
        );

        verify(resultado).close();
        verify(statement).close();
        verify(conexao).close();
    }
}