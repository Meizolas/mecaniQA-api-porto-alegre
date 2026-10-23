package br.com.mecaniqa.mapper;

import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdemServicoResultSetMapper {

    public static OrdemServico toModel(ResultSet resultado)
            throws SQLException {

        return OrdemServico.builder()
                .id(resultado.getLong("id"))
                .descricaoProblema(
                        resultado.getString("descricao_problema")
                )
                .dataAbertura(
                        resultado.getDate("data_abertura")
                                .toLocalDate()
                )
                .status(
                        StatusOrdemServico.valueOf(
                                resultado.getString("status")
                        )
                )
                .valor(
                        resultado.getBigDecimal("valor")
                                .doubleValue()
                )
                .build();
    }
}