package br.com.mecaniqa.dto.pedidopeca;

public class ItemPedidoPecaResponseDTO {

    private Long id;
    private Long codigoPeca;
    private String codigoBarrasPeca;
    private String fornecedorMarcaPeca;
    private Integer quantidade;
    private Double precoUnitario;
    private Double subtotal;

    public ItemPedidoPecaResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigoPeca() {
        return codigoPeca;
    }

    public void setCodigoPeca(Long codigoPeca) {
        this.codigoPeca = codigoPeca;
    }

    public String getCodigoBarrasPeca() {
        return codigoBarrasPeca;
    }

    public void setCodigoBarrasPeca(String codigoBarrasPeca) {
        this.codigoBarrasPeca = codigoBarrasPeca;
    }

    public String getFornecedorMarcaPeca() {
        return fornecedorMarcaPeca;
    }

    public void setFornecedorMarcaPeca(String fornecedorMarcaPeca) {
        this.fornecedorMarcaPeca = fornecedorMarcaPeca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
