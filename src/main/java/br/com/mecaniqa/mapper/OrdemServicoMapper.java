package br.com.mecaniqa.mapper;

import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.StatusOrdemServico;

public final class OrdemServicoMapper {
    private OrdemServicoMapper() {}

    public static OrdemServico paraModel(OrdemServicoRequestDTO dto) {
        return OrdemServico.builder()
                .descricaoProblema(dto.getDescricaoProblema())
                .status(StatusOrdemServico.ABERTO)
                .valor(dto.getValor() == null ? 0.0 : dto.getValor())
                .build();
    }

    public static OrdemServicoResponseDTO paraResponseDTO(OrdemServico ordem) {
        OrdemServicoResponseDTO dto = new OrdemServicoResponseDTO();
        dto.setId(ordem.getId());
        dto.setDescricaoProblema(ordem.getDescricaoProblema());
        dto.setDataAbertura(ordem.getDataAbertura());
        dto.setStatus(ordem.getStatus());
        dto.setValor(ordem.getValor());
        return dto;
    }
}
