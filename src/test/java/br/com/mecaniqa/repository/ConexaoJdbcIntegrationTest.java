package br.com.mecaniqa.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConexaoJdbcIntegrationTest {
    @Test
    void deveContarPecasNoBanco() {
        int quantidade = PecaRepository.getInstance().contar();
        
        assertEquals(0, quantidade);
    }
}
