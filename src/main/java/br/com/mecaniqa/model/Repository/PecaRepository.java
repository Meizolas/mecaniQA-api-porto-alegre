package br.com.mecaniqa.model.Repository;

import br.com.mecaniqa.model.Peca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PecaRepository {

    private static PecaRepository instance;

    private final List<Peca> pecas;

    private Long contadorId = 1L;

    private PecaRepository() {
        this.pecas = new ArrayList<>();
    }

    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public Peca adicionarPeca(Peca peca) {
        peca.setId(contadorId++);
        peca.setDataCadastro(LocalDateTime.now());
        peca.setDataAtualizacao(LocalDateTime.now());
        this.pecas.add(peca);
        return peca;
    }

    public List<Peca> listarPecas() {
        return this.pecas;
    }

    public Optional<Peca> buscarPorId(Long id) {
        return this.pecas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<Peca> atualizarPeca(Long id, Peca pecaAtualizada) {
        Optional<Peca> optExistente = buscarPorId(id);

        if (optExistente.isPresent()) {
            Peca existente = optExistente.get();

            existente.setNome(pecaAtualizada.getNome());
            existente.setCodigoBarras(pecaAtualizada.getCodigoBarras());
            existente.setFornecedor(pecaAtualizada.getFornecedor());
            existente.setPreco(pecaAtualizada.getPreco());
            existente.setQuantidadeEstoque(pecaAtualizada.getQuantidadeEstoque());
            existente.setCategoria(pecaAtualizada.getCategoria());
            existente.setDataAtualizacao(LocalDateTime.now());

            return Optional.of(existente);
        }

        return Optional.empty();
    }

    public boolean removerPeca(Long id) {
        return this.pecas.removeIf(p -> p.getId().equals(id));
    }
}