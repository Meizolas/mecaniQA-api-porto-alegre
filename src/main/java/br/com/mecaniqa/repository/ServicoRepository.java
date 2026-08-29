package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.Servico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoRepository {

    private static ServicoRepository instance;
    private final List<Servico> servicos;
    private Long proximoId;

    private ServicoRepository() {
        this.servicos = new ArrayList<>();
        this.proximoId = 1L;
    }

    public static ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }
        return instance;
    }

    public Servico salvar(Servico servico) {
        LocalDateTime agora = LocalDateTime.now();
        servico.setId(proximoId++);
        servico.setDataCadastro(agora);
        servico.setDataAtualizacao(agora);
        servicos.add(servico);
        return servico;
    }

    public List<Servico> listar() {
        return new ArrayList<>(servicos);
    }

    public Optional<Servico> buscarPorId(Long id) {
        return servicos.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst();
    }

    public Optional<Servico> atualizar(Long id, Servico novosDados) {
        Optional<Servico> servicoEncontrado = buscarPorId(id);
        
        if (servicoEncontrado.isEmpty()) {
            return Optional.empty();
        }
        
        Servico servico = servicoEncontrado.get();
        servico.setDescricao(novosDados.getDescricao());
        servico.setValorMaoDeObra(novosDados.getValorMaoDeObra());
        servico.setCustoTabelado(novosDados.getCustoTabelado());
        servico.settempoEstimadoMinutos(novosDados.gettempoEstimadoMinutos());
        servico.setPecasUtilizadas(novosDados.getPecasUtilizadas());
        servico.setDataAtualizacao(LocalDateTime.now());
        
        return Optional.of(servico);
    }

    public boolean excluir(Long id) {
        return servicos.removeIf(s -> s.getId().equals(id));
    }
}
