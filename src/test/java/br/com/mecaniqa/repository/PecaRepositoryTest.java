package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Peca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PecaRepositoryTest {

    private PecaRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = PecaRepository.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        repository = PecaRepository.getInstance();
    }

    @Test
    void deveSalvarPeca() {
        Peca peca = new Peca();
        peca.setCodigoBarras("123456");
        
        Peca salva = repository.salvar(peca);
        
        assertNotNull(salva.getCodigo());
        assertNotNull(salva.getDataCadastro());
        assertEquals("123456", salva.getCodigoBarras());
    }

    @Test
    void deveListarPecas() {
        repository.salvar(new Peca());
        repository.salvar(new Peca());
        
        List<Peca> pecas = repository.listar();
        
        assertEquals(2, pecas.size());
    }

    @Test
    void deveBuscarPorCodigo() {
        Peca peca = repository.salvar(new Peca());
        
        Optional<Peca> encontrada = repository.buscarPorCodigo(peca.getCodigo());
        
        assertTrue(encontrada.isPresent());
        assertEquals(peca.getCodigo(), encontrada.get().getCodigo());
    }

    @Test
    void deveAtualizarPeca() {
        Peca peca = new Peca();
        peca.setPrecoVenda(100.0);
        Peca salva = repository.salvar(peca);
        
        Peca novosDados = new Peca();
        novosDados.setPrecoVenda(150.0);
        
        Optional<Peca> atualizada = repository.atualizar(salva.getCodigo(), novosDados);
        
        assertTrue(atualizada.isPresent());
        assertEquals(150.0, atualizada.get().getPrecoVenda());
    }

    @Test
    void deveExcluirPeca() {
        Peca peca = repository.salvar(new Peca());
        
        boolean excluida = repository.excluir(peca.getCodigo());
        Optional<Peca> encontrada = repository.buscarPorCodigo(peca.getCodigo());
        
        assertTrue(excluida);
        assertFalse(encontrada.isPresent());
    }
}
