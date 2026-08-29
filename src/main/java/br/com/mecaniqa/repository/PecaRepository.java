package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Peca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PecaRepository {

    private static PecaRepository instance;

    private final List<Peca> pecas;
    private Long proximoCodigo;

    private PecaRepository() {
        this.pecas = new ArrayList<>();
        this.proximoCodigo = 1L;
    }

    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }

        return instance;
    }

    public Peca salvar(Peca peca) {
        LocalDateTime agora = LocalDateTime.now();

        peca.setCodigo(proximoCodigo++);
        peca.setDataCadastro(agora);
        peca.setDataUltimaAtualizacao(agora);

        pecas.add(peca);
        return peca;
    }

    public List<Peca> listar() {
        return new ArrayList<>(pecas);
    }

    public Optional<Peca> buscarPorCodigo(Long codigo) {
        return pecas.stream()
                .filter(peca -> peca.getCodigo().equals(codigo))
                .findFirst();
    }

    public Optional<Peca> atualizar(Long codigo, Peca novosDados) {
        Optional<Peca> pecaEncontrada = buscarPorCodigo(codigo);

        if (pecaEncontrada.isEmpty()) {
            return Optional.empty();
        }

        Peca peca = pecaEncontrada.get();

        peca.setCodigoBarras(novosDados.getCodigoBarras());
        peca.setFornecedorMarca(novosDados.getFornecedorMarca());
        peca.setQuantidadeEstoque(novosDados.getQuantidadeEstoque());
        peca.setPrecoCusto(novosDados.getPrecoCusto());
        peca.setPrecoVenda(novosDados.getPrecoVenda());
        peca.setTamanho(novosDados.getTamanho());
        peca.setCor(novosDados.getCor());
        peca.setCategoria(novosDados.getCategoria());
        peca.setDataUltimaAtualizacao(LocalDateTime.now());

        return Optional.of(peca);
    }

    public boolean excluir(Long codigo) {
        return pecas.removeIf(
                peca -> peca.getCodigo().equals(codigo)
        );
    }
}
