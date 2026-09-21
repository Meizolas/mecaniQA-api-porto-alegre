package br.com.mecaniqa.dto.pedidopeca;

public class ItemPedidoPecaRequestDTO {

    private Long codigoPeca;
    private Integer quantidade;

    public ItemPedidoPecaRequestDTO() {
    }

    public Long getCodigoPeca() {
        return codigoPeca;
    }

    public void setCodigoPeca(Long codigoPeca) {
        this.codigoPeca = codigoPeca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
