package br.com.mecaniqa.mapper;

import br.com.mecaniqa.model.Servico;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class ServicoResultSetMapper {
    private ServicoResultSetMapper() {
    }

    public static Servico toModel(ResultSet resultado) 
    throws SQLException {
        Servico servico = new Servico();
        servico.setId(resultado.getLong("id"));
        servico.setDescricao(resultado.getString("descricao"));
        servico.setValorMaoDeObra(paraDouble(resultado.getBigDecimal("valor_mao_de_obra")));
        servico.setCustoTabelado(paraDouble(resultado.getBigDecimal("custo_tabelado")));
        servico.setTempoEstimadoMinutos(
                paraDouble(resultado.getBigDecimal("tempo_estimado_minutos")));

        Timestamp cadastro = resultado.getTimestamp("data_cadastro");
        Timestamp atualizacao = resultado.getTimestamp("data_atualizacao");
        servico.setDataCadastro(cadastro == null ? null : cadastro.toLocalDateTime());
        servico.setDataAtualizacao(
                atualizacao == null ? null : atualizacao.toLocalDateTime());
        return servico;
    }

    private static Double paraDouble(BigDecimal valor) {
        return valor == null ? null : valor.doubleValue();
    }
}
