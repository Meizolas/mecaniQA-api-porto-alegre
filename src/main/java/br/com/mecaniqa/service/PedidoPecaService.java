package br.com.mecaniqa.service;

import br.com.mecaniqa.dto.pedidopeca.ItemPedidoPecaRequestDTO;
import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.exception.PecaNaoEncontradaException;
import br.com.mecaniqa.mapper.PedidoPecaMapper;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;
import br.com.mecaniqa.repository.PecaRepository;
import br.com.mecaniqa.repository.PedidoPecaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoPecaService {
    private final PedidoPecaRepository pedidoRepository;
    private final PecaRepository pecaRepository;

    public PedidoPecaService() {
        this(PedidoPecaRepository.getInstance(), PecaRepository.getInstance());
    }

    public PedidoPecaService(PedidoPecaRepository pedidoRepository, PecaRepository pecaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pecaRepository = pecaRepository;
    }

    public boolean excluir(Long id) {
        if (!pedidoRepository.existsById(id)) {
            return false;
        }
        pedidoRepository.deleteById(id);
        return true;
    }

    public PedidoPeca criar(PedidoPeca pedido, List<ItemPedidoPecaRequestDTO> itens) {
        if (itens != null && !itens.isEmpty()) {
            resolverItens(itens).forEach(pedido::adicionarItem);
        }
        return pedidoRepository.save(pedido);
    }

    public Optional<PedidoPeca> adicionarItens(
            Long id, List<ItemPedidoPecaRequestDTO> itens) {
        validarItensObrigatorios(itens);
        return pedidoRepository.adicionarItens(id, resolverItens(itens));
    }

    public Optional<PedidoPeca> atualizarStatus(Long id, StatusPedidoPeca status) {
        if (status == null) {
            throw new DadosInvalidosException("O status é obrigatório.");
        }
        return pedidoRepository.atualizarStatus(id, status);
    }

    public List<PedidoPeca> listar() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoPeca> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    private List<ItemPedidoPeca> resolverItens(List<ItemPedidoPecaRequestDTO> dtos) {
        validarItens(dtos);
        List<ItemPedidoPeca> itens = new ArrayList<>();
        for (ItemPedidoPecaRequestDTO dto : dtos) {
            Peca peca = pecaRepository.findById(dto.getCodigoPeca())
                    .orElseThrow(() -> new PecaNaoEncontradaException(dto.getCodigoPeca()));
            itens.add(PedidoPecaMapper.paraItemModel(dto, peca));
        }
        return itens;
    }

    private void validarItensObrigatorios(List<ItemPedidoPecaRequestDTO> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new DadosInvalidosException(
                    "Informe peças com código e quantidade maior que zero.");
        }
    }

    private void validarItens(List<ItemPedidoPecaRequestDTO> itens) {
        if (itens == null) {
            return;
        }
        if (itens.stream().anyMatch(item -> item == null
                || item.getCodigoPeca() == null
                || item.getQuantidade() == null
                || item.getQuantidade() <= 0)) {
            throw new DadosInvalidosException(
                    "Informe peças com código e quantidade maior que zero.");
        }
    }
}
