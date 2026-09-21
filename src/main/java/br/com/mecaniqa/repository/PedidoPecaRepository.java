package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoPecaRepository {

    private static PedidoPecaRepository instance;

    private final List<PedidoPeca> pedidos;
    private Long proximoIdPedido;
    private Long proximoIdItem;

    private PedidoPecaRepository() {
        this.pedidos = new ArrayList<>();
        this.proximoIdPedido = 1L;
        this.proximoIdItem = 1L;
    }

    public static PedidoPecaRepository getInstance() {
        if (instance == null) {
            instance = new PedidoPecaRepository();
        }
        return instance;
    }

    public PedidoPeca salvar(PedidoPeca pedido) {
        LocalDateTime agora = LocalDateTime.now();

        pedido.setId(proximoIdPedido++);
        pedido.setDataCriacao(agora);
        pedido.setDataUltimaAtualizacao(agora);

        for (ItemPedidoPeca item : pedido.getItens()) {
            if (item.getId() == null) {
                item.setId(proximoIdItem++);
            }
        }

        pedidos.add(pedido);
        return pedido;
    }

    public Optional<PedidoPeca> adicionarItens(Long id, List<ItemPedidoPeca> itens) {
        Optional<PedidoPeca> pedidoEncontrado = buscarPorId(id);

        if (pedidoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        PedidoPeca pedido = pedidoEncontrado.get();

        for (ItemPedidoPeca item : itens) {
            item.setId(proximoIdItem++);
            pedido.adicionarItem(item);
        }

        pedido.setDataUltimaAtualizacao(LocalDateTime.now());
        return Optional.of(pedido);
    }

    public Optional<PedidoPeca> atualizarStatus(Long id, StatusPedidoPeca novoStatus) {
        Optional<PedidoPeca> pedidoEncontrado = buscarPorId(id);

        if (pedidoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        PedidoPeca pedido = pedidoEncontrado.get();
        pedido.setStatus(novoStatus);
        pedido.setDataUltimaAtualizacao(LocalDateTime.now());

        return Optional.of(pedido);
    }

    public List<PedidoPeca> listar() {
        return new ArrayList<>(pedidos);
    }

    public Optional<PedidoPeca> buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
