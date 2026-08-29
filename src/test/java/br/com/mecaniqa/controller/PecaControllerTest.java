package br.com.mecaniqa.controller;

import br.com.mecaniqa.model.CategoriaPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.Repository.PecaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PecaControllerTest {

    private PecaController controller;
    private PecaRepository repository;

    @BeforeEach
    void setUp() {
        PecaRepository.resetInstance();
        repository = PecaRepository.getInstance();
        controller = new PecaController();
    }

    @Test
    void deveCriarPecaERetornar201() {
        Peca peca = criarPeca("Pastilha de Freio", "7891234567890", "Bosch", 89.90, 50, CategoriaPeca.FREIO);

        ResponseEntity<Peca> response = controller.criar(peca);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Pastilha de Freio", response.getBody().getNome());
    }

    @Test
    void deveListarTodasAsPecasERetornar200() {
        controller.criar(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));
        controller.criar(criarPeca("Amortecedor", "222", "Monroe", 320.00, 20, CategoriaPeca.SUSPENSAO));

        ResponseEntity<List<Peca>> response = controller.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        ResponseEntity<List<Peca>> response = controller.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deveBuscarPecaPorIdERetornar200() {
        controller.criar(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        ResponseEntity<Peca> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pastilha de Freio", response.getBody().getNome());
    }

    @Test
    void deveRetornar404QuandoPecaNaoExiste() {
        ResponseEntity<Peca> response = controller.buscarPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveAtualizarPecaERetornar200() {
        controller.criar(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        Peca dadosNovos = criarPeca("Pastilha Cerâmica", "111-V2", "Bosch", 129.90, 30, CategoriaPeca.FREIO);
        ResponseEntity<Peca> response = controller.atualizar(1L, dadosNovos);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pastilha Cerâmica", response.getBody().getNome());
        assertEquals(129.90, response.getBody().getPreco());
    }

    @Test
    void deveRetornar404AoAtualizarPecaInexistente() {
        Peca dadosNovos = criarPeca("Inexistente", "000", "Nenhum", 0.0, 0, CategoriaPeca.GERAL);

        ResponseEntity<Peca> response = controller.atualizar(999L, dadosNovos);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveRemoverPecaERetornar204() {
        controller.criar(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        ResponseEntity<Void> response = controller.remover(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveRetornar404AoRemoverPecaInexistente() {
        ResponseEntity<Void> response = controller.remover(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveManter201ComCamposPreenchidosAposCriacao() {
        Peca peca = criarPeca("Vela de Ignição", "333", "NGK", 45.00, 100, CategoriaPeca.MOTOR);

        ResponseEntity<Peca> response = controller.criar(peca);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Peca body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
        assertNotNull(body.getDataCadastro());
        assertNotNull(body.getDataAtualizacao());
        assertEquals("NGK", body.getFornecedor());
        assertEquals(CategoriaPeca.MOTOR, body.getCategoria());
    }

    private Peca criarPeca(String nome, String codigoBarras, String fornecedor,
                           Double preco, Integer quantidade, CategoriaPeca categoria) {
        Peca peca = new Peca();
        peca.setNome(nome);
        peca.setCodigoBarras(codigoBarras);
        peca.setFornecedor(fornecedor);
        peca.setPreco(preco);
        peca.setQuantidadeEstoque(quantidade);
        peca.setCategoria(categoria);
        return peca;
    }
}
