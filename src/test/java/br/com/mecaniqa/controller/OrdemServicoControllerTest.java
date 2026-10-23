package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.ordemservico.AtualizarStatusOrdemDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;
import br.com.mecaniqa.repository.OrdemServicoRepository;
import br.com.mecaniqa.service.OrdemServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Teste unitario: o repositorio (JDBC) e um mock, sem banco e sem singleton real.
 * O id gerado pelo banco e simulado no mock, sem assumir valores sequenciais.
 */
class OrdemServicoControllerTest {
    private OrdemServicoRepository repository;
    private OrdemServicoController controller;

    @BeforeEach
    void setUp() {
        repository = mock(OrdemServicoRepository.class);
        controller = new OrdemServicoController(new OrdemServicoService(repository));
    }

    @Test
    void deveCriarSemCamposInternosEAtualizarStatus() {
        when(repository.salvar(any())).thenAnswer(invocacao -> {
            OrdemServico ordem = invocacao.getArgument(0);
            ordem.setId(42L);
            return ordem;
        });

        OrdemServicoRequestDTO dto = new OrdemServicoRequestDTO();
        dto.setDescricaoProblema("Ruído no motor");
        dto.setValor(350.0);
        ResponseEntity<OrdemServicoResponseDTO> criada = controller.criar(dto);
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());
        assertEquals(42L, criada.getBody().getId());
        assertNotNull(criada.getBody().getDataAbertura());
        assertEquals(StatusOrdemServico.ABERTO, criada.getBody().getStatus());

        OrdemServico emExecucao = OrdemServico.builder().id(42L)
                .status(StatusOrdemServico.EM_EXECUCAO).build();
        when(repository.atualizarStatus(42L, StatusOrdemServico.EM_EXECUCAO))
                .thenReturn(Optional.of(emExecucao));

        AtualizarStatusOrdemDTO status = new AtualizarStatusOrdemDTO();
        status.setStatus(StatusOrdemServico.EM_EXECUCAO);
        ResponseEntity<OrdemServicoResponseDTO> atualizada =
                controller.atualizarStatus(criada.getBody().getId(), status);
        assertEquals(HttpStatus.OK, atualizada.getStatusCode());
        assertEquals(StatusOrdemServico.EM_EXECUCAO, atualizada.getBody().getStatus());
    }

    @Test
    void deveRetornarNotFoundParaOrdemInexistente() {
        when(repository.buscarPorId(999L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.NOT_FOUND, controller.buscarPorId(999L).getStatusCode());
    }

    @Test
    void deveRetornarBadRequestParaStatusNuloSemTocarNoBanco() {
        AtualizarStatusOrdemDTO status = new AtualizarStatusOrdemDTO();

        assertEquals(HttpStatus.BAD_REQUEST, controller.atualizarStatus(1L, status).getStatusCode());
        verifyNoInteractions(repository);
    }
}
