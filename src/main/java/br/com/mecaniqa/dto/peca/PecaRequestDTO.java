package br.com.mecaniqa.dto.peca;

import br.com.mecaniqa.model.CategoriaPeca;

public class PecaRequestDTO {

    private String codigoBarras;
    private String fornecedorMarca;
    private Integer quantidadeEstoque;
    private Double precoCusto;
    private Double precoVenda;
    private String tamanho;
    private String cor;
    private CategoriaPeca categoria;

    public PecaRequestDTO() {

    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getFornecedorMarca() {
        return fornecedorMarca;
    }

    public void setFornecedorMarca(String fornecedorMarca) {
        this.fornecedorMarca = fornecedorMarca;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(Double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public CategoriaPeca getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaPeca categoria) {
        this.categoria = categoria;
    }
}
