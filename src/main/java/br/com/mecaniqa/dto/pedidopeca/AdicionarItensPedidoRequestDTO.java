package br.com.mecaniqa.dto.pedidopeca;

import java.util.ArrayList;
import java.util.List;

public class AdicionarItensPedidoRequestDTO {

    private List<ItemPedidoPecaRequestDTO> itens;

    public AdicionarItensPedidoRequestDTO() {
        this.itens = new ArrayList<>();
    }

    public List<ItemPedidoPecaRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoPecaRequestDTO> itens) {
        this.itens = itens;
    }
}
