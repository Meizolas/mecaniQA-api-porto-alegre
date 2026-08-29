package br.com.mecaniqa.controller;

import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.repository.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PecaControllerTest {

    private PecaController controller;
    private PecaRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = PecaRepository.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        
        repository = PecaRepository.getInstance();
        controller = new PecaController();
    }

    @Test
    void deveCadastrarPeca() {
        Peca peca = new Peca();
        peca.setPrecoVenda(10.0);
        
        ResponseEntity<Peca> response = controller.cadastrar(peca);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCodigo());
    }

    @Test
    void deveListarPecas() {
        controller.cadastrar(new Peca());
        
        ResponseEntity<List<Peca>> response = controller.listar();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deveBuscarPorCodigoEncontrado() {
        Peca peca = controller.cadastrar(new Peca()).getBody();
        
        ResponseEntity<Peca> response = controller.buscarPorCodigo(peca.getCodigo());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(peca.getCodigo(), response.getBody().getCodigo());
    }

    @Test
    void deveRetornarNotFoundAoBuscarPorCodigoInexistente() {
        ResponseEntity<Peca> response = controller.buscarPorCodigo(999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveAtualizarPecaEncontrada() {
        Peca peca = controller.cadastrar(new Peca()).getBody();
        Peca novosDados = new Peca();
        novosDados.setPrecoVenda(200.0);
        
        ResponseEntity<Peca> response = controller.atualizar(peca.getCodigo(), novosDados);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200.0, response.getBody().getPrecoVenda());
    }

    @Test
    void deveRetornarNotFoundAoAtualizarInexistente() {
        ResponseEntity<Peca> response = controller.atualizar(999L, new Peca());
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveExcluirPecaEncontrada() {
        Peca peca = controller.cadastrar(new Peca()).getBody();
        
        ResponseEntity<Void> response = controller.excluir(peca.getCodigo());
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundAoExcluirInexistente() {
        ResponseEntity<Void> response = controller.excluir(999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
