package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória para {@link PedidoPeca}.
 * Implementa o padrão Singleton estático, seguindo exatamente o mesmo
 * padrão adotado em {@link PecaRepository}.
 */
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

    // -------------------------------------------------------------------------
    // US03 — Criar Pedido
    // -------------------------------------------------------------------------

    /**
     * Persiste um novo pedido em memória, atribuindo ID e timestamps automaticamente.
     *
     * @param pedido entidade a ser salva
     * @return o mesmo pedido com id e datas preenchidos
     */
    public PedidoPeca salvar(PedidoPeca pedido) {
        LocalDateTime agora = LocalDateTime.now();

        pedido.setId(proximoIdPedido++);
        pedido.setDataCriacao(agora);
        pedido.setDataUltimaAtualizacao(agora);

        // Atribui IDs aos itens que já vierem no pedido (criação com itens)
        for (ItemPedidoPeca item : pedido.getItens()) {
            if (item.getId() == null) {
                item.setId(proximoIdItem++);
            }
        }

        pedidos.add(pedido);
        return pedido;
    }

    // -------------------------------------------------------------------------
    // US04 — Adicionar Itens
    // -------------------------------------------------------------------------

    /**
     * Adiciona novos itens a um pedido já existente e atualiza o timestamp.
     *
     * @param id    identificador do pedido
     * @param itens lista de itens a adicionar
     * @return o pedido atualizado, ou {@link Optional#empty()} se não encontrado
     */
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

    // -------------------------------------------------------------------------
    // US05 — Modificar Status
    // -------------------------------------------------------------------------

    /**
     * Atualiza o status de um pedido e registra o timestamp da alteração.
     *
     * @param id        identificador do pedido
     * @param novoStatus novo status a aplicar
     * @return o pedido atualizado, ou {@link Optional#empty()} se não encontrado
     */
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

    // -------------------------------------------------------------------------
    // Operações de consulta
    // -------------------------------------------------------------------------

    public List<PedidoPeca> listar() {
        return new ArrayList<>(pedidos);
    }

    public Optional<PedidoPeca> buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
