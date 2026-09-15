package br.com.mecaniqa.mapper;

import br.com.mecaniqa.dto.pedidopeca.ItemPedidoPecaResponseDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaResponseDTO;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsável por converter entre as entidades de domínio
 * {@link PedidoPeca} / {@link ItemPedidoPeca} e seus respectivos DTOs de resposta.
 * Segue o mesmo padrão de métodos estáticos adotado em {@link PecaMapper}.
 */
public class PedidoPecaMapper {

    /**
     * Converte uma entidade {@link ItemPedidoPeca} em seu DTO de resposta.
     *
     * @param item entidade associativa a ser convertida
     * @return {@link ItemPedidoPecaResponseDTO} pronto para serialização
     */
    public static ItemPedidoPecaResponseDTO paraItemResponseDTO(ItemPedidoPeca item) {
        ItemPedidoPecaResponseDTO dto = new ItemPedidoPecaResponseDTO();

        dto.setId(item.getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());

        if (item.getPeca() != null) {
            dto.setCodigoPeca(item.getPeca().getCodigo());
            dto.setCodigoBarrasPeca(item.getPeca().getCodigoBarras());
            dto.setFornecedorMarcaPeca(item.getPeca().getFornecedorMarca());
        }

        return dto;
    }

    /**
     * Converte uma entidade {@link PedidoPeca} em seu DTO de resposta completo,
     * incluindo a lista de itens já convertida.
     *
     * @param pedido entidade a ser convertida
     * @return {@link PedidoPecaResponseDTO} pronto para serialização
     */
    public static PedidoPecaResponseDTO paraResponseDTO(PedidoPeca pedido) {
        PedidoPecaResponseDTO dto = new PedidoPecaResponseDTO();

        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setDataUltimaAtualizacao(pedido.getDataUltimaAtualizacao());
        dto.setValorTotal(pedido.getValorTotal());

        List<ItemPedidoPecaResponseDTO> itensDTO = new ArrayList<>();
        for (ItemPedidoPeca item : pedido.getItens()) {
            itensDTO.add(paraItemResponseDTO(item));
        }
        dto.setItens(itensDTO);

        return dto;
    }
}
