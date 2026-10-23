package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.repository.PecaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Teste unitario: o repositorio (JDBC) e substituido por um mock, entao nao ha
 * banco, nao ha dados residuais e nao ha dependencia da ordem dos testes.
 */
class PecaControllerTest {
    private MockedStatic<PecaRepository> fabrica;
    private PecaRepository repository;
    private PecaController controller;

    @BeforeEach
    void setUp() {
        repository = mock(PecaRepository.class);
        fabrica = mockStatic(PecaRepository.class);
        fabrica.when(PecaRepository::getInstance).thenReturn(repository);
        controller = new PecaController();
    }

    @AfterEach
    void tearDown() {
        fabrica.close();
    }

    @Test
    void deveCadastrarBuscarAtualizarEExcluirPecaComDtos() {
        when(repository.salvar(any())).thenAnswer(invocacao -> {
            Peca peca = invocacao.getArgument(0);
            peca.setCodigo(10L);
            return peca;
        });
        ResponseEntity<PecaResponseDTO> criada = controller.cadastrar(novaPeca(10.0));
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());
        assertEquals(10L, criada.getBody().getCodigo());

        Peca existente = new Peca();
        existente.setCodigo(10L);
        existente.setPrecoVenda(10.0);
        when(repository.findById(10L)).thenReturn(Optional.of(existente));
        assertEquals(HttpStatus.OK, controller.buscarPorCodigo(10L).getStatusCode());

        when(repository.listar()).thenReturn(List.of(existente));
        assertEquals(1, controller.listar().getBody().size());

        when(repository.atualizar(eq(10L), any())).thenAnswer(invocacao -> {
            Peca novosDados = invocacao.getArgument(1);
            novosDados.setCodigo(10L);
            return Optional.of(novosDados);
        });
        ResponseEntity<PecaResponseDTO> atualizada = controller.atualizar(10L, novaPeca(200.0));
        assertEquals(200.0, atualizada.getBody().getPrecoVenda());

        when(repository.excluir(10L)).thenReturn(true);
        assertEquals(HttpStatus.NO_CONTENT, controller.excluir(10L).getStatusCode());
    }

    @Test
    void deveRetornarNotFoundParaCodigoInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        when(repository.atualizar(eq(999L), any())).thenReturn(Optional.empty());
        when(repository.excluir(999L)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, controller.buscarPorCodigo(999L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.atualizar(999L, novaPeca(1.0)).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.excluir(999L).getStatusCode());
    }

    @Test
    void deveRetornarBadRequestParaCodigoInvalidoSemConsultarOBanco() {
        assertEquals(HttpStatus.BAD_REQUEST, controller.buscarPorCodigo(0L).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, controller.atualizar(-1L, novaPeca(1.0)).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, controller.excluir(0L).getStatusCode());

        verifyNoInteractions(repository);
    }

    private PecaRequestDTO novaPeca(double preco) {
        PecaRequestDTO dto = new PecaRequestDTO();
        dto.setPrecoVenda(preco);
        return dto;
    }
}
