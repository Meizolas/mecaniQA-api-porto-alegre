package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Peca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
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
    void deveTerConstrutorPrivado() throws Exception {
        Constructor<PecaRepository> constructor = PecaRepository.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void deveRetornarMesmaInstancia() {
        PecaRepository outraReferencia = PecaRepository.getInstance();

        assertSame(repository, outraReferencia);
    }

    @Test
    void deveCompartilharDadosEntreReferencias() {
        PecaRepository outraReferencia = PecaRepository.getInstance();
        repository.salvar(new Peca());

        assertEquals(1, outraReferencia.listar().size());
    }

    @Test
    void deveSalvarPeca() {
        Peca peca = new Peca();
        peca.setCodigoBarras("123456");
        
        Peca salva = repository.salvar(peca);
        
        assertNotNull(salva.getCodigo());
        assertNotNull(salva.getDataCadastro());
        assertNotNull(salva.getDataUltimaAtualizacao());
        assertEquals("123456", salva.getCodigoBarras());
    }

    @Test
    void deveGerarCodigosNaoRepetidos() {
        Peca primeira = repository.salvar(new Peca());
        Peca segunda = repository.salvar(new Peca());

        assertNotEquals(primeira.getCodigo(), segunda.getCodigo());
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
    void deveRetornarVazioAoBuscarCodigoInexistente() {
        Optional<Peca> encontrada = repository.buscarPorCodigo(999L);

        assertTrue(encontrada.isEmpty());
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
    void devePreservarCodigoEDataCadastroAoAtualizar() {
        Peca salva = repository.salvar(new Peca());
        Long codigoOriginal = salva.getCodigo();
        LocalDateTime dataCadastroOriginal = salva.getDataCadastro();

        Peca novosDados = new Peca();
        novosDados.setCodigo(999L);
        novosDados.setDataCadastro(LocalDateTime.now().plusDays(1));

        Peca atualizada = repository.atualizar(codigoOriginal, novosDados).orElseThrow();

        assertEquals(codigoOriginal, atualizada.getCodigo());
        assertEquals(dataCadastroOriginal, atualizada.getDataCadastro());
    }

    @Test
    void deveAlterarDataDaUltimaAtualizacao() {
        Peca salva = repository.salvar(new Peca());
        LocalDateTime dataAnterior = LocalDateTime.now().minusDays(1);
        salva.setDataUltimaAtualizacao(dataAnterior);

        Peca atualizada = repository.atualizar(salva.getCodigo(), new Peca()).orElseThrow();

        assertTrue(atualizada.getDataUltimaAtualizacao().isAfter(dataAnterior));
    }

    @Test
    void deveExcluirPeca() {
        Peca peca = repository.salvar(new Peca());
        
        boolean excluida = repository.excluir(peca.getCodigo());
        Optional<Peca> encontrada = repository.buscarPorCodigo(peca.getCodigo());
        
        assertTrue(excluida);
        assertFalse(encontrada.isPresent());
    }

    @Test
    void deveRetornarFalseAoExcluirCodigoInexistente() {
        assertFalse(repository.excluir(999L));
    }
}
