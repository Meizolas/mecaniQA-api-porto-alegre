package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.ordemservico.AtualizarStatusOrdemDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.model.StatusOrdemServico;
import br.com.mecaniqa.repository.OrdemServicoRepository;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoControllerTest {
    private OrdemServicoController controller;

    @BeforeEach
    void setUp() throws Exception {
        Field instance = OrdemServicoRepository.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        controller = new OrdemServicoController();
    }

    @Test
    void deveCriarSemCamposInternosEAtualizarStatus() {
        OrdemServicoRequestDTO dto = new OrdemServicoRequestDTO();
        dto.setDescricaoProblema("Ruído no motor");
        dto.setValor(350.0);
        ResponseEntity<OrdemServicoResponseDTO> criada = controller.criar(dto);
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());
        assertNotNull(criada.getBody().getId());
        assertNotNull(criada.getBody().getDataAbertura());
        assertEquals(StatusOrdemServico.ABERTO, criada.getBody().getStatus());

        AtualizarStatusOrdemDTO status = new AtualizarStatusOrdemDTO();
        status.setStatus(StatusOrdemServico.EM_EXECUCAO);
        assertEquals(HttpStatus.OK, controller.atualizarStatus(criada.getBody().getId(), status).getStatusCode());
    }

    @Test
    void deveRetornarNotFoundParaOrdemInexistente() {
        assertEquals(HttpStatus.NOT_FOUND, controller.buscarPorId(999L).getStatusCode());
    }
}
