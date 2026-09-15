package br.com.mecaniqa.dto.pedidopeca;

import br.com.mecaniqa.model.StatusPedidoPeca;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoPecaResponseDTO {

    private Long id;
    private StatusPedidoPeca status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;
    private List<ItemPedidoPecaResponseDTO> itens;
    private Double valorTotal;

    public PedidoPecaResponseDTO() {
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

    public List<ItemPedidoPecaResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoPecaResponseDTO> itens) {
        this.itens = itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
