package br.com.mecaniqa.dto.ordemservico;

public class OrdemServicoRequestDTO {
    private String descricaoProblema;
    private Double valor;

    public OrdemServicoRequestDTO() {}
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
