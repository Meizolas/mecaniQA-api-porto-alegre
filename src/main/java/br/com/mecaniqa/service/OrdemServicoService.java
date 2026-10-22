package br.com.mecaniqa.service;

import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;
import br.com.mecaniqa.repository.OrdemServicoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrdemServicoService {

    private final OrdemServicoRepository repository;

    public OrdemServicoService() {
        this(OrdemServicoRepository.getInstance());
    }

    public OrdemServicoService(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public Optional<OrdemServico> atualizar(Long id, String descricaoProblema, Double valor) {
        if (descricaoProblema == null || descricaoProblema.isBlank()
                || valor == null || !Double.isFinite(valor) || valor < 0) {
            throw new DadosInvalidosException(
                    "Informe a descrição do problema e um valor válido maior ou igual a zero.");
        }

        return repository.buscarPorId(id).map(ordem -> {
            ordem.setDescricaoProblema(descricaoProblema);
            ordem.setValor(valor);
            return repository.update(ordem);
        });
    }

    public boolean excluir(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public OrdemServico criar(OrdemServico ordem) {
        ordem.setDataAbertura(LocalDate.now());

        if (ordem.getStatus() == null) {
            ordem.setStatus(StatusOrdemServico.ABERTO);
        }

        return repository.salvar(ordem);
    }

    public Optional<OrdemServico> atualizarStatus(
            Long id,
            StatusOrdemServico status) {

        if (status == null) {
            throw new DadosInvalidosException(
                    "O status da ordem de serviço é obrigatório."
            );
        }

        return repository.atualizarStatus(id, status);
    }

    public List<OrdemServico> listar() {
        return repository.listar();
    }

    public Optional<OrdemServico> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }
}
