package br.com.mecaniqa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void adicionarItem(ItemPedidoPeca item) {
        this.itens.add(item);
    }

    public Double getValorTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getSubtotal())
                .sum();
    }
}
