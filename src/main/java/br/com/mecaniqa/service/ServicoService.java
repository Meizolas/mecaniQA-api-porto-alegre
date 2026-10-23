package br.com.mecaniqa.service;

import br.com.mecaniqa.model.Servico;
import br.com.mecaniqa.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

public class ServicoService {
    private final ServicoRepository repository;

    public ServicoService() {
        this.repository = ServicoRepository.getInstance();
    }

    public Servico cadastrar(Servico servico) {
        return repository.save(servico);
    }

    public List<Servico> listar() {
        return repository.findAll();
    }

    public Optional<Servico> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Servico> atualizar(Long id, Servico servico) {
        return repository.atualizar(id, servico);
    }

    public boolean excluir(Long id) {
        return repository.excluir(id);
    }
}
