package br.com.mecaniqa.dto.pedidopeca;

import br.com.mecaniqa.model.StatusPedidoPeca;

/**
 * DTO de requisição para atualizar o status de um Pedido de Peças (US05).
 * O cliente informa apenas o novo status desejado.
 */
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
