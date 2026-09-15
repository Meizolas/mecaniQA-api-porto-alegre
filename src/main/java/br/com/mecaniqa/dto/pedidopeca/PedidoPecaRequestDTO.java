package br.com.mecaniqa.dto.pedidopeca;

import java.util.ArrayList;
import java.util.List;

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
