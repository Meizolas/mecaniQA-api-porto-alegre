package br.com.mecaniqa.mapper;


import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.model.Peca;

public class PecaMapper {
    public static Peca paraModel(PecaRequestDTO dto) {
        Peca peca = new Peca();

        peca.setCodigoBarras(dto.getCodigoBarras());
        peca.setFornecedorMarca(dto.getFornecedorMarca());
        peca.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        peca.setPrecoCusto(dto.getPrecoCusto());
        peca.setPrecoVenda(dto.getPrecoVenda());
        peca.setTamanho(dto.getTamanho());
        peca.setCor(dto.getCor());
        peca.setCategoria(dto.getCategoria());

        return peca;
    }

    public static PecaResponseDTO paraResponseDTO(Peca peca) {
        PecaResponseDTO dto = new PecaResponseDTO();

        dto.setCodigo(peca.getCodigo());
        dto.setCodigoBarras(peca.getCodigoBarras());
        dto.setFornecedorMarca(peca.getFornecedorMarca());
        dto.setQuantidadeEstoque(peca.getQuantidadeEstoque());
        dto.setPrecoCusto(peca.getPrecoCusto());
        dto.setPrecoVenda(peca.getPrecoVenda());
        dto.setDataCadastro(peca.getDataCadastro());
        dto.setDataUltimaAtualizacao(peca.getDataUltimaAtualizacao());
        dto.setTamanho(peca.getTamanho());
        dto.setCor(peca.getCor());
        dto.setCategoria(peca.getCategoria());

        return dto;
    }
}
