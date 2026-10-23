package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.pedidopeca.AdicionarItensPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.AtualizarStatusPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.ItemPedidoPecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaRequestDTO;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;
import br.com.mecaniqa.repository.PecaRepository;
import br.com.mecaniqa.repository.PedidoPecaRepository;
import br.com.mecaniqa.service.PedidoPecaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Teste unitario: repositorios (JDBC) mockados. Ids de pedido e de peca sao
 * definidos pelo proprio teste, sem depender de contador em memoria.
 */
class PedidoPecaControllerTest {
    private static final long CODIGO_PECA = 5L;
    private static final long ID_PEDIDO = 8L;

    private PedidoPecaRepository pedidos;
    private PecaRepository pecas;
    private PedidoPecaController controller;
    private Peca peca;

    @BeforeEach
    void setUp() {
        pedidos = mock(PedidoPecaRepository.class);
        pecas = mock(PecaRepository.class);
        controller = new PedidoPecaController(new PedidoPecaService(pedidos, pecas));

        peca = new Peca();
        peca.setCodigo(CODIGO_PECA);
        peca.setPrecoVenda(25.0);
        when(pecas.findById(CODIGO_PECA)).thenReturn(Optional.of(peca));
    }

    @Test
    void deveCriarAdicionarItemEAtualizarStatus() {
        when(pedidos.save(any())).thenAnswer(invocacao -> {
            PedidoPeca pedido = invocacao.getArgument(0);
            pedido.setId(ID_PEDIDO);
            return pedido;
        });
        ResponseEntity<?> criada = controller.criarPedido(new PedidoPecaRequestDTO());
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());

        PedidoPeca comItem = new PedidoPeca();
        comItem.setId(ID_PEDIDO);
        comItem.adicionarItem(new ItemPedidoPeca(peca, 3));
        when(pedidos.adicionarItens(eq(ID_PEDIDO), any())).thenReturn(Optional.of(comItem));

        AdicionarItensPedidoRequestDTO itens = new AdicionarItensPedidoRequestDTO();
        itens.setItens(List.of(item(CODIGO_PECA, 3)));
        assertEquals(HttpStatus.OK, controller.adicionarItens(ID_PEDIDO, itens).getStatusCode());

        PedidoPeca entregue = new PedidoPeca();
        entregue.setId(ID_PEDIDO);
        entregue.setStatus(StatusPedidoPeca.ENTREGUE);
        when(pedidos.atualizarStatus(ID_PEDIDO, StatusPedidoPeca.ENTREGUE))
                .thenReturn(Optional.of(entregue));

        AtualizarStatusPedidoRequestDTO status = new AtualizarStatusPedidoRequestDTO();
        status.setStatus(StatusPedidoPeca.ENTREGUE);
        assertEquals(HttpStatus.OK, controller.atualizarStatus(ID_PEDIDO, status).getStatusCode());
    }

    @Test
    void deveTratarPedidoPecaEQuantidadeInvalidos() {
        when(pedidos.adicionarItens(eq(999L), any())).thenReturn(Optional.empty());
        when(pecas.findById(999L)).thenReturn(Optional.empty());

        AdicionarItensPedidoRequestDTO itens = new AdicionarItensPedidoRequestDTO();
        itens.setItens(List.of(item(CODIGO_PECA, 1)));
        assertEquals(HttpStatus.NOT_FOUND, controller.adicionarItens(999L, itens).getStatusCode());

        itens.setItens(List.of(item(999L, 1)));
        assertEquals(HttpStatus.NOT_FOUND, controller.adicionarItens(ID_PEDIDO, itens).getStatusCode());

        itens.setItens(List.of(item(CODIGO_PECA, 0)));
        assertEquals(HttpStatus.BAD_REQUEST, controller.adicionarItens(ID_PEDIDO, itens).getStatusCode());

        verify(pedidos, never()).adicionarItens(eq(ID_PEDIDO), any());
    }

    private ItemPedidoPecaRequestDTO item(Long codigo, int quantidade) {
        ItemPedidoPecaRequestDTO item = new ItemPedidoPecaRequestDTO();
        item.setCodigoPeca(codigo);
        item.setQuantidade(quantidade);
        return item;
    }
}
