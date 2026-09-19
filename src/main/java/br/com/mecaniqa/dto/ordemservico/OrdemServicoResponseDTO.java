package br.com.mecaniqa.dto.ordemservico;

import br.com.mecaniqa.model.StatusOrdemServico;
import java.time.LocalDate;

public class OrdemServicoResponseDTO {
    private Long id;
    private String descricaoProblema;
    private LocalDate dataAbertura;
    private StatusOrdemServico status;
    private Double valor;

    public OrdemServicoResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    public StatusOrdemServico getStatus() { return status; }
    public void setStatus(StatusOrdemServico status) { this.status = status; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
