package br.com.mecaniqa.dto.servico;

import java.time.LocalDateTime;

public class ServicoResponseDTO {
    private Long id;
    private String descricao;
    private Double valorMaoDeObra;
    private Double custoTabelado;
    private Double tempoEstimadoMinutos;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public ServicoResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getValorMaoDeObra() { return valorMaoDeObra; }
    public void setValorMaoDeObra(Double valorMaoDeObra) { this.valorMaoDeObra = valorMaoDeObra; }
    public Double getCustoTabelado() { return custoTabelado; }
    public void setCustoTabelado(Double custoTabelado) { this.custoTabelado = custoTabelado; }
    public Double getTempoEstimadoMinutos() { return tempoEstimadoMinutos; }
    public void setTempoEstimadoMinutos(Double tempoEstimadoMinutos) { this.tempoEstimadoMinutos = tempoEstimadoMinutos; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
