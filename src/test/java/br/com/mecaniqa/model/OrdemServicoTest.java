package br.com.mecaniqa.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrdemServicoTest {
    @Test
    void deveCriarOrdemComBuilder() {
        OrdemServico ordem = OrdemServico.builder()
                .descricaoProblema("Ruído no motor")
                .dataAbertura(LocalDate.now())
                .status(StatusOrdemServico.ABERTO)
                .valor(350.0)
                .build();

        assertNotNull(ordem);
        assertEquals("Ruído no motor", ordem.getDescricaoProblema());
        assertEquals(StatusOrdemServico.ABERTO, ordem.getStatus());
        assertEquals(350.0, ordem.getValor());
    }
}
