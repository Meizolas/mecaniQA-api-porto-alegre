package br.com.mecaniqa.model.Repository;

import br.com.mecaniqa.model.CategoriaPeca;
import br.com.mecaniqa.model.Peca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PecaRepositoryTest {

    private PecaRepository repository;

    @BeforeEach
    void setUp() {
        PecaRepository.resetInstance();
        repository = PecaRepository.getInstance();
    }

    @Test
    void deveRetornarMesmaInstanciaSingleton() {
        PecaRepository outraReferencia = PecaRepository.getInstance();
        assertSame(repository, outraReferencia);
    }

    @Test
    void deveAdicionarPecaComIdGerado() {
        Peca peca = criarPeca("Pastilha de Freio", "7891234567890", "Bosch", 89.90, 50, CategoriaPeca.FREIO);

        Peca salva = repository.adicionarPeca(peca);

        assertNotNull(salva.getId());
        assertEquals(1L, salva.getId());
        assertNotNull(salva.getDataCadastro());
        assertNotNull(salva.getDataAtualizacao());
    }

    @Test
    void deveIncrementarIdAutomaticamente() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));
        repository.adicionarPeca(criarPeca("Amortecedor", "222", "Monroe", 320.00, 20, CategoriaPeca.SUSPENSAO));

        List<Peca> pecas = repository.listarPecas();

        assertEquals(2, pecas.size());
        assertEquals(1L, pecas.get(0).getId());
        assertEquals(2L, pecas.get(1).getId());
    }

    @Test
    void deveListarTodasAsPecas() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));
        repository.adicionarPeca(criarPeca("Amortecedor", "222", "Monroe", 320.00, 20, CategoriaPeca.SUSPENSAO));
        repository.adicionarPeca(criarPeca("Vela de Ignição", "333", "NGK", 45.00, 100, CategoriaPeca.MOTOR));

        List<Peca> pecas = repository.listarPecas();

        assertEquals(3, pecas.size());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaPecas() {
        List<Peca> pecas = repository.listarPecas();

        assertNotNull(pecas);
        assertTrue(pecas.isEmpty());
    }

    @Test
    void deveBuscarPecaPorIdExistente() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        Optional<Peca> resultado = repository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Pastilha de Freio", resultado.get().getNome());
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        Optional<Peca> resultado = repository.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarPecaExistente() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        Peca dadosNovos = criarPeca("Pastilha de Freio Cerâmica", "111-V2", "Bosch", 129.90, 30, CategoriaPeca.FREIO);
        Optional<Peca> atualizada = repository.atualizarPeca(1L, dadosNovos);

        assertTrue(atualizada.isPresent());
        assertEquals("Pastilha de Freio Cerâmica", atualizada.get().getNome());
        assertEquals(129.90, atualizada.get().getPreco());
        assertEquals(30, atualizada.get().getQuantidadeEstoque());
        assertNotNull(atualizada.get().getDataAtualizacao());
    }

    @Test
    void deveRetornarVazioAoAtualizarIdInexistente() {
        Peca dadosNovos = criarPeca("Inexistente", "000", "Nenhum", 0.0, 0, CategoriaPeca.GERAL);
        Optional<Peca> resultado = repository.atualizarPeca(999L, dadosNovos);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRemoverPecaExistente() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        boolean removido = repository.removerPeca(1L);

        assertTrue(removido);
        assertTrue(repository.listarPecas().isEmpty());
    }

    @Test
    void deveRetornarFalseAoRemoverIdInexistente() {
        boolean removido = repository.removerPeca(999L);

        assertFalse(removido);
    }

    @Test
    void devePreservarIdOriginalAposAtualizacao() {
        repository.adicionarPeca(criarPeca("Pastilha de Freio", "111", "Bosch", 89.90, 50, CategoriaPeca.FREIO));

        Peca dadosNovos = criarPeca("Pastilha Atualizada", "222", "Bosch", 99.90, 40, CategoriaPeca.FREIO);
        Optional<Peca> atualizada = repository.atualizarPeca(1L, dadosNovos);

        assertTrue(atualizada.isPresent());
        assertEquals(1L, atualizada.get().getId());
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
