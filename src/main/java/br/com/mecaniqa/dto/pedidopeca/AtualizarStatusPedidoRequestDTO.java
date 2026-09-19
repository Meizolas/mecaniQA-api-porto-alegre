package br.com.mecaniqa.dto.pedidopeca;

import br.com.mecaniqa.model.StatusPedidoPeca;

public class AtualizarStatusPedidoRequestDTO {

    private StatusPedidoPeca status;

    public AtualizarStatusPedidoRequestDTO() {
    }

    public StatusPedidoPeca getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoPeca status) {
        this.status = status;
    }
}
