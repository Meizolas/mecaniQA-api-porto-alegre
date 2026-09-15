package br.com.mecaniqa.dto.pedidopeca;

/**
 * DTO de requisição para um item dentro de um Pedido de Peças.
 * O cliente informa o ID da peça existente e a quantidade desejada.
 */
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
