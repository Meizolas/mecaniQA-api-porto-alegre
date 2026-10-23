package br.com.mecaniqa.repository;

import br.com.mecaniqa.config.Conexao;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdemServicoCrudJdbcTest {
    @Test
    void updateJdbcAlteraSomenteDescricaoEValor() throws Exception {
        Connection conexao = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(conexao.prepareStatement(anyString())).thenReturn(statement);
        OrdemServico ordem = OrdemServico.builder().id(7L).descricaoProblema("Revisão")
                .valor(250).dataAbertura(LocalDate.of(2026, 8, 14))
                .status(StatusOrdemServico.EM_EXECUCAO).build();

        try (var fabrica = mockStatic(Conexao.class)) {
            fabrica.when(Conexao::getConnection).thenReturn(conexao);
            assertSame(ordem, OrdemServicoRepository.getInstance().update(ordem));
        }

        ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
        verify(conexao).prepareStatement(sql.capture());
        assertEquals("UPDATE ordens_servico SET descricao_problema = ?, valor = ? WHERE id = ?",
                sql.getValue().replaceAll("\\s+", " ").trim());
        verify(statement).setString(1, "Revisão");
        verify(statement).setBigDecimal(2, BigDecimal.valueOf(250.0));
        verify(statement).setLong(3, 7L);
        verify(statement).executeUpdate();
        verify(statement).close();
        verify(conexao).close();
    }

    @Test
    void deleteJdbcExcluiPeloId() throws Exception {
        Connection conexao = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(conexao.prepareStatement("DELETE FROM ordens_servico WHERE id = ?"))
                .thenReturn(statement);
        try (var fabrica = mockStatic(Conexao.class)) {
            fabrica.when(Conexao::getConnection).thenReturn(conexao);
            OrdemServicoRepository.getInstance().deleteById(7L);
        }
        verify(statement).setLong(1, 7L);
        verify(statement).executeUpdate();
        verify(statement).close();
        verify(conexao).close();
    }
}
