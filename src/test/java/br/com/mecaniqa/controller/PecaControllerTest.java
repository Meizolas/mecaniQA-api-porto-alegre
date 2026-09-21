package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.repository.PecaRepository;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class PecaControllerTest {
    private PecaController controller;

    @BeforeEach
    void setUp() throws Exception {
        Field instance = PecaRepository.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        controller = new PecaController();
    }

    @Test
    void deveCadastrarBuscarAtualizarEExcluirPecaComDtos() {
        PecaRequestDTO dto = novaPeca(10.0);
        ResponseEntity<PecaResponseDTO> criada = controller.cadastrar(dto);
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());
        assertNotNull(criada.getBody().getCodigo());

        Long codigo = criada.getBody().getCodigo();
        assertEquals(HttpStatus.OK, controller.buscarPorCodigo(codigo).getStatusCode());
        assertEquals(1, controller.listar().getBody().size());

        ResponseEntity<PecaResponseDTO> atualizada = controller.atualizar(codigo, novaPeca(200.0));
        assertEquals(200.0, atualizada.getBody().getPrecoVenda());
        assertEquals(HttpStatus.NO_CONTENT, controller.excluir(codigo).getStatusCode());
    }

    @Test
    void deveRetornarNotFoundParaCodigoInexistente() {
        assertEquals(HttpStatus.NOT_FOUND, controller.buscarPorCodigo(999L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.atualizar(999L, novaPeca(1.0)).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.excluir(999L).getStatusCode());
    }

    private PecaRequestDTO novaPeca(double preco) {
        PecaRequestDTO dto = new PecaRequestDTO();
        dto.setPrecoVenda(preco);
        return dto;
    }
}
