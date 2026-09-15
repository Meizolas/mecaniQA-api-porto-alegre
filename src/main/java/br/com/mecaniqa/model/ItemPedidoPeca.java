package br.com.mecaniqa.model;

/**
 * Entidade associativa que representa um item dentro de um Pedido de Peças.
 * Relaciona uma Peca (já existente) a um PedidoPeca, registrando
 * a quantidade desejada e o preço unitário no momento da inclusão.
 */
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

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

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

    /**
     * Calcula o subtotal deste item (precoUnitario * quantidade).
     */
    public Double getSubtotal() {
        if (precoUnitario == null || quantidade == null) {
            return 0.0;
        }
        return precoUnitario * quantidade;
    }
}
