package br.com.mecaniqa.model;

public class ItemPedidoPeca {

    private Long id;
    private Peca peca;
    private Integer quantidade;
    private Double precoUnitario;

    public ItemPedidoPeca() {
    }

    public ItemPedidoPeca(Peca peca, Integer quantidade) {
        this.peca = peca;
        this.quantidade = quantidade;
        this.precoUnitario = peca.getPrecoVenda();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
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
        if (precoUnitario == null || quantidade == null) {
            return 0.0;
        }
        return precoUnitario * quantidade;
    }
}git checkout main
