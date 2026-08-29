package br.com.mecaniqa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Servico {
    private Long id;
    private String descricao;
    private Double valorMaoDeObra;
    private Double custoTabelado;
    private Double tempoEstimadoMinutos;
    private List<Peca> pecasUtilizadas;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public Servico() {
        this.pecasUtilizadas = new ArrayList<>();
    }

    public Servico(String descricao, Double valorMaoDeObra, Double custoTabelado,
                   Double tempoEstimadoMinutos, List<Peca> pecasUtilizadas) {
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
        this.custoTabelado = custoTabelado;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.pecasUtilizadas = pecasUtilizadas == null
                ? new ArrayList<>()
                : new ArrayList<>(pecasUtilizadas);
    }

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
    public List<Peca> getPecasUtilizadas() { return pecasUtilizadas; }

    public void setPecasUtilizadas(List<Peca> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas == null
                ? new ArrayList<>()
                : new ArrayList<>(pecasUtilizadas);
    }

    public void adicionarPeca(Peca peca) { this.pecasUtilizadas.add(peca); }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
