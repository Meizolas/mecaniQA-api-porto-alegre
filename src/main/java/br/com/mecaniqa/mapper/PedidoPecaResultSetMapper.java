package br.com.mecaniqa.mapper;

import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.StatusPedidoPeca;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PedidoPecaResultSetMapper {

    /**
     * Converte uma linha da tabela pedidos_pecas em PedidoPeca.
     * Os itens são carregados à parte pelo repositório.
     */
    public static PedidoPeca toModel(ResultSet resultado)
            throws SQLException {

        PedidoPeca pedido = new PedidoPeca();

        pedido.setId(resultado.getLong("id"));
        pedido.setStatus(
                StatusPedidoPeca.valueOf(resultado.getString("status"))
        );

        Timestamp criacao = resultado.getTimestamp("data_criacao");
        Timestamp atualizacao =
                resultado.getTimestamp("data_ultima_atualizacao");

        pedido.setDataCriacao(
                criacao == null ? null : criacao.toLocalDateTime()
        );
        pedido.setDataUltimaAtualizacao(
                atualizacao == null ? null : atualizacao.toLocalDateTime()
        );

        return pedido;
    }

    /**
     * Converte uma linha do JOIN itens_pedido_peca + pecas em
     * ItemPedidoPeca, reaproveitando o PecaResultSetMapper para a peça.
     */
    public static ItemPedidoPeca toItemModel(ResultSet resultado)
            throws SQLException {

        ItemPedidoPeca item = new ItemPedidoPeca();

        item.setId(resultado.getLong("item_id"));
        item.setQuantidade(resultado.getInt("quantidade"));

        BigDecimal preco = resultado.getBigDecimal("preco_unitario");
        item.setPrecoUnitario(preco == null ? null : preco.doubleValue());

        item.setPeca(PecaResultSetMapper.toModel(resultado));

        return item;
    }
}
