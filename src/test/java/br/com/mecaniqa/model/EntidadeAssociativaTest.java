package br.com.mecaniqa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários da entidade associativa ItemPedidoPeca (Peça x Pedido)
 * e das regras de cálculo do PedidoPeca.
 */
@DisplayName("Entidade Associativa - ItemPedidoPeca / PedidoPeca")
class EntidadeAssociativaTest {

    private Peca peca(double precoVenda) {
        Peca peca = new Peca();
        peca.setPrecoVenda(precoVenda);
        return peca;
    }

    @Test
    @DisplayName("Item liga a Peça ao Pedido guardando quantidade e preço unitário")
    void itemDeveGuardarPecaQuantidadeEPreco() {
        Peca peca = peca(25.0);
        ItemPedidoPeca item = new ItemPedidoPeca(peca, 4);

        assertSame(peca, item.getPeca());
        assertEquals(4, item.getQuantidade());
        assertEquals(25.0, item.getPrecoUnitario());
        assertEquals(100.0, item.getSubtotal());
    }

    @Test
    @DisplayName("Preço unitário é uma cópia: mudar o preço da peça depois não altera o item")
    void precoUnitarioNaoDeveMudarComAPeca() {
        Peca peca = peca(25.0);
        ItemPedidoPeca item = new ItemPedidoPeca(peca, 2);

        peca.setPrecoVenda(999.0);

        assertEquals(25.0, item.getPrecoUnitario());
        assertEquals(50.0, item.getSubtotal());
    }

    @Test
    @DisplayName("Subtotal é 0.0 quando preço ou quantidade são nulos (sem NullPointerException)")
    void subtotalDeveSerZeroComCamposNulos() {
        assertEquals(0.0, new ItemPedidoPeca().getSubtotal());
        assertEquals(0.0, new ItemPedidoPeca(new Peca(), 3).getSubtotal());
    }

    @Test
    @DisplayName("Pedido novo nasce ORCANDO, sem itens e com total 0")
    void pedidoNovoDeveNascerOrcando() {
        PedidoPeca pedido = new PedidoPeca();

        assertEquals(StatusPedidoPeca.ORCANDO, pedido.getStatus());
        assertTrue(pedido.getItens().isEmpty());
        assertEquals(0.0, pedido.getValorTotal());
    }

    @Test
    @DisplayName("Pedido aceita várias peças diferentes e a mesma peça mais de uma vez")
    void pedidoDeveAceitarMultiplosItens() {
        Peca filtro = peca(35.0);
        Peca pastilha = peca(80.0);
        PedidoPeca pedido = new PedidoPeca();

        pedido.adicionarItem(new ItemPedidoPeca(filtro, 2));
        pedido.adicionarItem(new ItemPedidoPeca(pastilha, 1));
        pedido.adicionarItem(new ItemPedidoPeca(filtro, 1));

        assertEquals(3, pedido.getItens().size());
        assertEquals(35.0 * 3 + 80.0, pedido.getValorTotal());
    }

    @Test
    @DisplayName("Enum StatusPedidoPeca possui exatamente os 4 status obrigatórios")
    void statusObrigatoriosDoPedido() {
        assertEquals(4, StatusPedidoPeca.values().length);
        StatusPedidoPeca.valueOf("ORCANDO");
        StatusPedidoPeca.valueOf("PENDENTE_PAGAMENTO");
        StatusPedidoPeca.valueOf("PAGO_FATURADO");
        StatusPedidoPeca.valueOf("ENTREGUE");
    }
}
