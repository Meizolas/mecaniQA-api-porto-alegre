package br.com.mecaniqa.dto.pedidopeca;

import br.com.mecaniqa.model.StatusPedidoPeca;

public class AtualizarStatusPedidoRequestDTO {

    private StatusPedidoPeca novoStatus;

    public AtualizarStatusPedidoRequestDTO() {
    }

    public StatusPedidoPeca getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(StatusPedidoPeca novoStatus) {
        this.novoStatus = novoStatus;
    }
}
