package br.com.mecaniqa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade principal que representa um Pedido de Peças.
 * Agrega uma lista de {@link ItemPedidoPeca} e controla o status do pedido
 * por meio do enum {@link StatusPedidoPeca}.
 */
public class PedidoPeca {

    private Long id;
    private StatusPedidoPeca status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;
    private List<ItemPedidoPeca> itens;

    public PedidoPeca() {
        this.itens = new ArrayList<>();
        this.status = StatusPedidoPeca.ORCANDO;
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPedidoPeca getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoPeca status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public List<ItemPedidoPeca> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoPeca> itens) {
        this.itens = itens;
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------------------

    /**
     * Adiciona um item à lista de itens do pedido.
     */
    public void adicionarItem(ItemPedidoPeca item) {
        this.itens.add(item);
    }

    /**
     * Calcula o valor total do pedido somando os subtotais de todos os itens.
     */
    public Double getValorTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getSubtotal())
                .sum();
    }
}
