package br.com.mecaniqa.dto.ordemservico;

import br.com.mecaniqa.model.StatusOrdemServico;

public class AtualizarStatusOrdemDTO {
    private StatusOrdemServico status;

    public AtualizarStatusOrdemDTO() {}
    public StatusOrdemServico getStatus() { return status; }
    public void setStatus(StatusOrdemServico status) { this.status = status; }
}
