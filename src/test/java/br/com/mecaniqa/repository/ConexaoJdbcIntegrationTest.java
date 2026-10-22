package br.com.mecaniqa.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConexaoJdbcIntegrationTest {
    @Test
    void deveContarPecasNoBanco() {
        int quantidade = PecaRepository.getInstance().contar();
        
        assertTrue(quantidade >= 0);
    }
}
