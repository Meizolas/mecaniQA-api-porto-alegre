package br.com.mecaniqa.repository;

import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.PedidoPeca;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PedidoPecaRepositoryTransacaoTest {

    private static class RepositorioTeste extends PedidoPecaRepository {
        private final Connection conexao;

        RepositorioTeste(Connection conexao) {
            this.conexao = conexao;
        }

        @Override
        protected Connection abrirConexao() {
            return conexao;
        }
    }

    @Test
    void deveSalvarPedidoEItensNaMesmaConexaoEConfirmar() throws SQLException {
        Connection conexao = mock(Connection.class);
        PreparedStatement cabecalho = statementComId(11L);
        PreparedStatement item1 = statementComId(21L);
        PreparedStatement item2 = statementComId(22L);
        when(conexao.getAutoCommit()).thenReturn(true);
        when(conexao.prepareStatement(anyString(), anyInt()))
                .thenReturn(cabecalho, item1, item2);

        PedidoPeca pedido = pedidoComDoisItens();
        new RepositorioTeste(conexao).save(pedido);

        assertEquals(11L, pedido.getId());
        assertEquals(21L, pedido.getItens().get(0).getId());
        assertEquals(22L, pedido.getItens().get(1).getId());
        verify(conexao).commit();
        verify(conexao, never()).rollback();
        verify(conexao).close();
        inOrder(cabecalho, item1, item2)
                .verify(cabecalho).executeUpdate();
    }

    @Test
    void deveReverterPedidoQuandoUmItemFalha() throws SQLException {
        Connection conexao = mock(Connection.class);
        PreparedStatement cabecalho = statementComId(11L);
        PreparedStatement item1 = statementComId(21L);
        PreparedStatement item2 = mock(PreparedStatement.class);
        when(item2.executeUpdate()).thenThrow(new SQLException("Falha no segundo item"));
        when(conexao.getAutoCommit()).thenReturn(true);
        when(conexao.prepareStatement(anyString(), anyInt()))
                .thenReturn(cabecalho, item1, item2);

        assertThrows(
                RuntimeException.class,
                () -> new RepositorioTeste(conexao).save(pedidoComDoisItens()));

        verify(conexao, never()).commit();
        verify(conexao).rollback();
        verify(cabecalho).close();
        verify(item1).close();
        verify(item2).close();
        verify(conexao).close();
    }

    private PreparedStatement statementComId(long id) throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet chaves = mock(ResultSet.class);
        when(statement.executeUpdate()).thenReturn(1);
        when(statement.getGeneratedKeys()).thenReturn(chaves);
        when(chaves.next()).thenReturn(true);
        when(chaves.getLong(1)).thenReturn(id);
        return statement;
    }

    private PedidoPeca pedidoComDoisItens() {
        Peca peca = new Peca();
        peca.setCodigo(5L);
        peca.setPrecoVenda(12.50);

        PedidoPeca pedido = new PedidoPeca();
        pedido.adicionarItem(new ItemPedidoPeca(peca, 1));
        pedido.adicionarItem(new ItemPedidoPeca(peca, 2));
        return pedido;
    }
}
