package br.com.mecaniqa.model.Repository;

import br.com.mecaniqa.model.Servico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoRepository {

    private static ServicoRepository instance;

    private final List<Servico> servicos;

    private Long contadorId = 1L;

    private ServicoRepository() {
        this.servicos = new ArrayList<>();
    }

    public static ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public Servico adicionarServico(Servico servico) {
        servico.setId(contadorId++);
        servico.setDataCadastro(LocalDateTime.now());
        servico.setDataAtualizacao(LocalDateTime.now());
        this.servicos.add(servico);
        return servico;
    }

    public List<Servico> listarServicos() {
        return this.servicos;
    }

    public Optional<Servico> buscarPorId(Long id) {
        return this.servicos.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Optional<Servico> atualizarServico(Long id, Servico servicoAtualizado) {
        Optional<Servico> optExistente = buscarPorId(id);

        if (optExistente.isPresent()) {
            Servico existente = optExistente.get();

            existente.setDescricao(servicoAtualizado.getDescricao());
            existente.setValorMaoDeObra(servicoAtualizado.getValorMaoDeObra());
            existente.setCustoTabelado(servicoAtualizado.getCustoTabelado());
            existente.settempoEstimadoMinutos(servicoAtualizado.gettempoEstimadoMinutos());
            existente.setPecasUtilizadas(servicoAtualizado.getPecasUtilizadas());
            existente.setDataAtualizacao(LocalDateTime.now());

            return Optional.of(existente);
        }

        return Optional.empty();
    }

    public boolean removerServico(Long id) {
        return this.servicos.removeIf(s -> s.getId().equals(id));
    }
}