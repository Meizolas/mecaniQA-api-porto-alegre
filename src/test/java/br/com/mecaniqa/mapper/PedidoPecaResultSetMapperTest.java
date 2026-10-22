package br.com.mecaniqa.mapper;

import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PedidoPecaResultSetMapperTest {

    @Test
    void deveConverterLinhaDoPedido() throws SQLException {
        LocalDateTime criacao = LocalDateTime.of(2026, 10, 21, 10, 0);
        LocalDateTime atualizacao = criacao.plusHours(2);

        ResultSet resultado = mock(ResultSet.class);
        when(resultado.getLong("id")).thenReturn(7L);
        when(resultado.getString("status")).thenReturn("PAGO_FATURADO");
        when(resultado.getTimestamp("data_criacao"))
                .thenReturn(Timestamp.valueOf(criacao));
        when(resultado.getTimestamp("data_ultima_atualizacao"))
                .thenReturn(Timestamp.valueOf(atualizacao));

        PedidoPeca pedido = PedidoPecaResultSetMapper.toModel(resultado);

        assertEquals(7L, pedido.getId());
        assertEquals(StatusPedidoPeca.PAGO_FATURADO, pedido.getStatus());
        assertEquals(criacao, pedido.getDataCriacao());
        assertEquals(atualizacao, pedido.getDataUltimaAtualizacao());
        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    void deveConverterLinhaDoItemComAPeca() throws SQLException {
        ResultSet resultado = mock(ResultSet.class);
        when(resultado.getLong("item_id")).thenReturn(3L);
        when(resultado.getInt("quantidade")).thenReturn(2);
        when(resultado.getBigDecimal("preco_unitario"))
                .thenReturn(new BigDecimal("25.50"));
        when(resultado.getLong("codigo")).thenReturn(10L);
        when(resultado.getString("fornecedor_marca")).thenReturn("Bosch");
        when(resultado.getString("categoria")).thenReturn(null);

        ItemPedidoPeca item = PedidoPecaResultSetMapper.toItemModel(resultado);

        assertEquals(3L, item.getId());
        assertEquals(2, item.getQuantidade());
        assertEquals(25.50, item.getPrecoUnitario());
        assertEquals(51.0, item.getSubtotal());
        assertEquals(10L, item.getPeca().getCodigo());
        assertEquals("Bosch", item.getPeca().getFornecedorMarca());
        assertNull(item.getPeca().getCategoria());
    }

    @Test
    void deveAceitarPrecoUnitarioNulo() throws SQLException {
        ResultSet resultado = mock(ResultSet.class);
        when(resultado.getLong("item_id")).thenReturn(1L);
        when(resultado.getInt("quantidade")).thenReturn(4);
        when(resultado.getBigDecimal("preco_unitario")).thenReturn(null);

        ItemPedidoPeca item = PedidoPecaResultSetMapper.toItemModel(resultado);

        assertNull(item.getPrecoUnitario());
        assertEquals(0.0, item.getSubtotal());
    }
}
