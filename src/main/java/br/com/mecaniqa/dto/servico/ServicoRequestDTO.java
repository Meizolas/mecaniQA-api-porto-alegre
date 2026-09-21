package br.com.mecaniqa.dto.servico;

public class ServicoRequestDTO {
    private String descricao;
    private Double valorMaoDeObra;
    private Double custoTabelado;
    private Double tempoEstimadoMinutos;

    public ServicoRequestDTO() {}
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getValorMaoDeObra() { return valorMaoDeObra; }
    public void setValorMaoDeObra(Double valorMaoDeObra) { this.valorMaoDeObra = valorMaoDeObra; }
    public Double getCustoTabelado() { return custoTabelado; }
    public void setCustoTabelado(Double custoTabelado) { this.custoTabelado = custoTabelado; }
    public Double getTempoEstimadoMinutos() { return tempoEstimadoMinutos; }
    public void setTempoEstimadoMinutos(Double tempoEstimadoMinutos) { this.tempoEstimadoMinutos = tempoEstimadoMinutos; }
}
