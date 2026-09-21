package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.AdicionarItensPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.AtualizarStatusPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.ItemPedidoPecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaRequestDTO;
import br.com.mecaniqa.model.StatusPedidoPeca;
import br.com.mecaniqa.repository.PecaRepository;
import br.com.mecaniqa.repository.PedidoPecaRepository;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoPecaControllerTest {
    private PedidoPecaController controller;
    private Long codigoPeca;

    @BeforeEach
    void setUp() throws Exception {
        resetarSingleton(PecaRepository.class);
        resetarSingleton(PedidoPecaRepository.class);
        PecaRequestDTO peca = new PecaRequestDTO();
        peca.setPrecoVenda(25.0);
        codigoPeca = new PecaController().cadastrar(peca).getBody().getCodigo();
        controller = new PedidoPecaController();
    }

    @Test
    void deveCriarAdicionarItemEAtualizarStatus() {
        ResponseEntity<?> criada = controller.criarPedido(new PedidoPecaRequestDTO());
        assertEquals(HttpStatus.CREATED, criada.getStatusCode());

        AdicionarItensPedidoRequestDTO itens = new AdicionarItensPedidoRequestDTO();
        itens.setItens(List.of(item(codigoPeca, 3)));
        assertEquals(HttpStatus.OK, controller.adicionarItens(1L, itens).getStatusCode());

        AtualizarStatusPedidoRequestDTO status = new AtualizarStatusPedidoRequestDTO();
        status.setStatus(StatusPedidoPeca.ENTREGUE);
        assertEquals(HttpStatus.OK, controller.atualizarStatus(1L, status).getStatusCode());
    }

    @Test
    void deveTratarPedidoPecaEQuantidadeInvalidos() {
        AdicionarItensPedidoRequestDTO itens = new AdicionarItensPedidoRequestDTO();
        itens.setItens(List.of(item(codigoPeca, 1)));
        assertEquals(HttpStatus.NOT_FOUND, controller.adicionarItens(999L, itens).getStatusCode());

        controller.criarPedido(new PedidoPecaRequestDTO());
        itens.setItens(List.of(item(999L, 1)));
        assertEquals(HttpStatus.NOT_FOUND, controller.adicionarItens(1L, itens).getStatusCode());
        itens.setItens(List.of(item(codigoPeca, 0)));
        assertEquals(HttpStatus.BAD_REQUEST, controller.adicionarItens(1L, itens).getStatusCode());
    }

    private ItemPedidoPecaRequestDTO item(Long codigo, int quantidade) {
        ItemPedidoPecaRequestDTO item = new ItemPedidoPecaRequestDTO();
        item.setCodigoPeca(codigo);
        item.setQuantidade(quantidade);
        return item;
    }

    private void resetarSingleton(Class<?> tipo) throws Exception {
        Field instance = tipo.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }
}
