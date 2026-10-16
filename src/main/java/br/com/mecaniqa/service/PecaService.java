package br.com.mecaniqa.service;

import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.exception.PecaNaoEncontradaException;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.repository.PecaRepository;

import java.util.List;

public class PecaService {

    private final PecaRepository repository;

    public PecaService() {
        repository = PecaRepository.getInstance();
    }

    public Peca cadastrar(Peca peca) {
        return repository.salvar(peca);
    }

    public List<Peca> listar() {
        return repository.listar();
    }

    public Peca buscarPorCodigo(Long codigo) {
        validarCodigo(codigo);

        return repository.findById(codigo)
                .orElseThrow(
                        () -> new PecaNaoEncontradaException(codigo)
                );
    }

    public Peca atualizar(Long codigo, Peca novosDados) {
        validarCodigo(codigo);

        return repository.atualizar(codigo, novosDados)
                .orElseThrow(
                        () -> new PecaNaoEncontradaException(codigo)
                );
    }

    public void excluir(Long codigo) {
        validarCodigo(codigo);

        if (!repository.excluir(codigo)) {
            throw new PecaNaoEncontradaException(codigo);
        }
    }

    private void validarCodigo(Long codigo) {
        if (codigo == null || codigo <= 0) {
            throw new DadosInvalidosException(
                    "O código da peça deve ser maior que zero."
            );
        }
    }
}