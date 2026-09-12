package br.com.mecaniqa.model;
 
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.assertNotNull;
 
class OrdemServicoTest {
 
    @Test
    void deveCriarComBuilder() {
        OrdemServico os = OrdemServico.builder().build();
        assertNotNull(os);
    }
}