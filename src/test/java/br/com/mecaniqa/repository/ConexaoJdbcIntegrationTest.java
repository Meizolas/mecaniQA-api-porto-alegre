package br.com.mecaniqa.repository;

import br.com.mecaniqa.suporte.TesteIntegracao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** INTEGRACAO: confirma a conexao JDBC com o MySQL isolado (somente leitura). */
@TesteIntegracao
public class ConexaoJdbcIntegrationTest {
    @Test
    void deveContarPecasNoBanco() {
        int quantidade = PecaRepository.getInstance().contar();
        
        assertTrue(quantidade >= 0);
    }
}
