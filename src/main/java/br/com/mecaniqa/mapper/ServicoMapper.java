package br.com.mecaniqa.mapper;

import br.com.mecaniqa.dto.servico.ServicoRequestDTO;
import br.com.mecaniqa.dto.servico.ServicoResponseDTO;
import br.com.mecaniqa.model.Servico;

public final class ServicoMapper {
    private ServicoMapper() {}

    public static Servico paraModel(ServicoRequestDTO dto) {
        Servico servico = new Servico();
        servico.setDescricao(dto.getDescricao());
        servico.setValorMaoDeObra(dto.getValorMaoDeObra());
        servico.setCustoTabelado(dto.getCustoTabelado());
        servico.setTempoEstimadoMinutos(dto.getTempoEstimadoMinutos());
        return servico;
    }

    public static ServicoResponseDTO paraResponseDTO(Servico servico) {
        ServicoResponseDTO dto = new ServicoResponseDTO();
        dto.setId(servico.getId());
        dto.setDescricao(servico.getDescricao());
        dto.setValorMaoDeObra(servico.getValorMaoDeObra());
        dto.setCustoTabelado(servico.getCustoTabelado());
        dto.setTempoEstimadoMinutos(servico.getTempoEstimadoMinutos());
        dto.setDataCadastro(servico.getDataCadastro());
        dto.setDataAtualizacao(servico.getDataAtualizacao());
        return dto;
    }
}
