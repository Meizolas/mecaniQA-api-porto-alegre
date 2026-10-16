package br.com.mecaniqa.mapper;

import br.com.mecaniqa.model.CategoriaPeca;
import br.com.mecaniqa.model.Peca;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PecaResultSetMapper {

    public static Peca toModel(ResultSet resultado)
            throws SQLException {

        Peca peca = new Peca();

        peca.setCodigo(resultado.getLong("codigo"));
        peca.setCodigoBarras(
                resultado.getString("codigo_barras")
        );
        peca.setFornecedorMarca(
                resultado.getString("fornecedor_marca")
        );

        peca.setQuantidadeEstoque(
                resultado.getObject("quantidade_estoque", Integer.class)
        );

        java.math.BigDecimal custo =
                resultado.getBigDecimal("preco_custo");

        java.math.BigDecimal venda =
                resultado.getBigDecimal("preco_venda");

        peca.setPrecoCusto(
                custo == null ? null : custo.doubleValue()
        );
        peca.setPrecoVenda(
                venda == null ? null : venda.doubleValue()
        );

        Timestamp cadastro =
                resultado.getTimestamp("data_cadastro");

        Timestamp atualizacao =
                resultado.getTimestamp("data_ultima_atualizacao");

        peca.setDataCadastro(
                cadastro == null ? null : cadastro.toLocalDateTime()
        );
        peca.setDataUltimaAtualizacao(
                atualizacao == null
                        ? null
                        : atualizacao.toLocalDateTime()
        );

        peca.setTamanho(resultado.getString("tamanho"));
        peca.setCor(resultado.getString("cor"));

        String categoria = resultado.getString("categoria");

        peca.setCategoria(
                categoria == null
                        ? null
                        : CategoriaPeca.valueOf(categoria)
        );

        return peca;
    }
}