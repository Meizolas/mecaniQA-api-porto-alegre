package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdemServicoRepository {
    private static OrdemServicoRepository instance;
    private final List<OrdemServico> ordens;
    private long proximoId;

    private OrdemServicoRepository() {
        ordens = new ArrayList<>();
        proximoId = 1L;
    }

    public static OrdemServicoRepository getInstance() {
        if (instance == null) instance = new OrdemServicoRepository();
        return instance;
    }

    public OrdemServico salvar(OrdemServico ordem) {
        ordem.setId(proximoId++);
        ordem.setDataAbertura(LocalDate.now());
        if (ordem.getStatus() == null) ordem.setStatus(StatusOrdemServico.ABERTO);
        ordens.add(ordem);
        return ordem;
    }

    public List<OrdemServico> listar() { return new ArrayList<>(ordens); }

    public Optional<OrdemServico> buscarPorId(Long id) {
        return ordens.stream().filter(ordem -> ordem.getId().equals(id)).findFirst();
    }

    public Optional<OrdemServico> atualizarStatus(Long id, StatusOrdemServico status) {
        Optional<OrdemServico> encontrada = buscarPorId(id);
        encontrada.ifPresent(ordem -> ordem.setStatus(status));
        return encontrada;
    }
}
