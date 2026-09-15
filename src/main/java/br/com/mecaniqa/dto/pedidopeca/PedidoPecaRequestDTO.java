package br.com.mecaniqa.dto.pedidopeca;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de requisição para criar um novo Pedido de Peças (US03).
 * O cliente pode opcionalmente já informar itens na criação do pedido.
 */
public class PedidoPecaRequestDTO {

    private List<ItemPedidoPecaRequestDTO> itens;

    public PedidoPecaRequestDTO() {
        this.itens = new ArrayList<>();
    }

    public List<ItemPedidoPecaRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoPecaRequestDTO> itens) {
        this.itens = itens;
    }
}
