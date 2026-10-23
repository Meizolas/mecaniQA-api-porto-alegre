package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.suporte.TesteIntegracao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRACAO: usa MySQL real. Rode com {@code .\gradlew integrationTest} apontando
 * para o banco isolado. Nao assume tabela vazia nem codigos sequenciais; cada teste
 * remove somente as pecas que ele mesmo criou.
 */
@TesteIntegracao
@DisplayName("Integração - PecaRepository (MySQL isolado)")
class PecaRepositoryIntegrationTest {

    private PecaRepository repository;
    private final List<Long> criadas = new ArrayList<>();

    @BeforeEach
    void setUp() {
        repository = PecaRepository.getInstance();
    }

    @AfterEach
    void limpar() {
        for (Long codigo : criadas) {
            repository.deleteById(codigo);
        }
        criadas.clear();
    }

    private Peca criar(Peca peca) {
        Peca salva = repository.salvar(peca);
        criadas.add(salva.getCodigo());
        return salva;
    }

    @Test
    void deveContarPecasNoBanco() {
        assertTrue(repository.contar() >= 0);
    }

    @Test
    void deveCompartilharDadosEntreReferencias() {
        Peca salva = criar(new Peca());

        assertTrue(PecaRepository.getInstance().buscarPorCodigo(salva.getCodigo()).isPresent());
    }

    @Test
    void deveSalvarPeca() {
        Peca peca = new Peca();
        peca.setCodigoBarras("123456");

        Peca salva = criar(peca);

        assertNotNull(salva.getCodigo());
        assertNotNull(salva.getDataCadastro());
        assertNotNull(salva.getDataUltimaAtualizacao());
        assertEquals("123456", salva.getCodigoBarras());
    }

    @Test
    void deveGerarCodigosNaoRepetidos() {
        Peca primeira = criar(new Peca());
        Peca segunda = criar(new Peca());

        assertNotEquals(primeira.getCodigo(), segunda.getCodigo());
    }

    @Test
    void deveListarPecasSalvas() {
        Peca primeira = criar(new Peca());
        Peca segunda = criar(new Peca());

        List<Long> codigos = repository.listar().stream().map(Peca::getCodigo).toList();

        assertTrue(codigos.contains(primeira.getCodigo()));
        assertTrue(codigos.contains(segunda.getCodigo()));
    }

    @Test
    void deveBuscarPorCodigo() {
        Peca peca = criar(new Peca());

        Optional<Peca> encontrada = repository.buscarPorCodigo(peca.getCodigo());

        assertTrue(encontrada.isPresent());
        assertEquals(peca.getCodigo(), encontrada.get().getCodigo());
    }

    @Test
    void deveRetornarVazioAoBuscarCodigoInexistente() {
        assertTrue(repository.buscarPorCodigo(Long.MAX_VALUE).isEmpty());
    }

    @Test
    void deveAtualizarPeca() {
        Peca peca = new Peca();
        peca.setPrecoVenda(100.0);
        Peca salva = criar(peca);

        Peca novosDados = new Peca();
        novosDados.setPrecoVenda(150.0);

        Optional<Peca> atualizada = repository.atualizar(salva.getCodigo(), novosDados);

        assertTrue(atualizada.isPresent());
        assertEquals(150.0, atualizada.get().getPrecoVenda());
    }

    @Test
    void devePreservarCodigoEDataCadastroAoAtualizar() {
        Peca salva = criar(new Peca());
        Long codigoOriginal = salva.getCodigo();
        LocalDateTime dataCadastroOriginal = salva.getDataCadastro();

        Peca novosDados = new Peca();
        novosDados.setCodigo(Long.MAX_VALUE);
        novosDados.setDataCadastro(LocalDateTime.now().plusDays(1));

        Peca atualizada = repository.atualizar(codigoOriginal, novosDados).orElseThrow();

        assertEquals(codigoOriginal, atualizada.getCodigo());
        assertEquals(dataCadastroOriginal, atualizada.getDataCadastro());
    }

    @Test
    void naoDeveRegredirDataDaUltimaAtualizacaoAoAtualizar() {
        Peca salva = criar(new Peca());

        Peca atualizada = repository.atualizar(salva.getCodigo(), new Peca()).orElseThrow();

        assertFalse(atualizada.getDataUltimaAtualizacao().isBefore(
                salva.getDataUltimaAtualizacao().minusSeconds(1)));
    }

    @Test
    void deveExcluirPeca() {
        Peca peca = criar(new Peca());

        boolean excluida = repository.excluir(peca.getCodigo());
        Optional<Peca> encontrada = repository.buscarPorCodigo(peca.getCodigo());

        assertTrue(excluida);
        assertFalse(encontrada.isPresent());
    }

    @Test
    void deveRetornarFalseAoExcluirCodigoInexistente() {
        assertFalse(repository.excluir(Long.MAX_VALUE));
    }
}

