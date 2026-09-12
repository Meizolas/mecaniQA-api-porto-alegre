package br.com.mecaniqa.model;

import java.time.LocalDate;
import java.util.List;

public class OrdemServico {

    private Long id;
    private String descricaoProblema;
    private LocalDate dataAbertura;
    private StatusOrdemServico status;
    private double valor;

    private OrdemServico(Builder builder) {
        this.id = builder.id;
        this.descricaoProblema = builder.descricaoProblema;
        this.dataAbertura = builder.dataAbertura;
        this.status = builder.status;
        this.valor = builder.valor;
    }

      public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

    private long id;
    private String descricaoProblema;
    private LocalDate dataAbertura;
    private StatusOrdemServico status;
    private double valor;
    

    public Builder id(long id) {
    this.id = id;
    return this;
    }

    public Builder descricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
        return this;
    }

    public Builder dataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
        return this;
    }

    public Builder status(StatusOrdemServico status) {
        this.status = status;
        return this;
    }

    public Builder valor(double valor) {
        this.valor = valor;
        return this;
    }

    public OrdemServico build() {
        return new OrdemServico(this);
    }
    }

    public OrdemServico(
        Long id,
        String descricaoProblema,
        LocalDate dataAbertura,
        StatusOrdemServico status,
        double valor) {

    this.id = id;
    this.descricaoProblema = descricaoProblema;
    this.dataAbertura = dataAbertura;
    this.status = status;
    this.valor = valor;
    }

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }

    public String getDescricaoProblema() {
    return descricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
    this.descricaoProblema = descricaoProblema;
    }

    public LocalDate getDataAbertura() {
    return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
    this.dataAbertura = dataAbertura;
    }

    public StatusOrdemServico getStatus() {
    return status;
    }

    public void setStatus(StatusOrdemServico status) {
    this.status = status;
    }

    public double getValor() {
    return valor;
    }

    public void setValor(double valor) {
    this.valor = valor;
    }
}
