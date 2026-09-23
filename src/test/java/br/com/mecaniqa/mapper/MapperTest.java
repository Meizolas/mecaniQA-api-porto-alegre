package br.com.mecaniqa.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaResponseDTO;
import br.com.mecaniqa.dto.servico.ServicoRequestDTO;
import br.com.mecaniqa.model.CategoriaPeca;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.Servico;
import br.com.mecaniqa.model.StatusOrdemServico;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testes unitários dos Mappers (DTO <-> Model).
 */
@DisplayName("Mappers - conversão DTO <-> Model")
class MapperTest {

    @ParameterizedTest(name = "{0} expõe somente métodos públicos estáticos (classe utilitária)")
    @ValueSource(classes = {OrdemServicoMapper.class, PecaMapper.class, PedidoPecaMapper.class, ServicoMapper.class})
    void mappersDevemTerSomenteMetodosEstaticos(Class<?> mapper) {
        for (Method metodo : mapper.getDeclaredMethods()) {
            if (Modifier.isPublic(metodo.getModifiers())) {
                assertTrue(Modifier.isStatic(metodo.getModifiers()),
                        mapper.getSimpleName() + "." + metodo.getName() + " deveria ser static");
            }
        }
    }

    @Test
    @DisplayName("OrdemServicoMapper.paraModel usa o Builder, força status ABERTO e valor padrão 0.0")
    void ordemParaModel() {
        OrdemServicoRequestDTO dto = new OrdemServicoRequestDTO();
        dto.setDescricaoProblema("Motor falhando");

        OrdemServico ordem = OrdemServicoMapper.paraModel(dto);

        assertEquals("Motor falhando", ordem.getDescricaoProblema());
        assertEquals(StatusOrdemServico.ABERTO, ordem.getStatus());
        assertEquals(0.0, ordem.getValor());
    }

    @Test
    @DisplayName("OrdemServicoMapper.paraResponseDTO copia todos os campos")
    void ordemParaResponse() {
        LocalDate hoje = LocalDate.now();
        OrdemServico ordem = OrdemServico.builder()
                .id(7L).descricaoProblema("Freio").dataAbertura(hoje)
                .status(StatusOrdemServico.PAGO).valor(200.0).build();

        OrdemServicoResponseDTO dto = OrdemServicoMapper.paraResponseDTO(ordem);

        assertEquals(7L, dto.getId());
        assertEquals("Freio", dto.getDescricaoProblema());
        assertEquals(hoje, dto.getDataAbertura());
        assertEquals(StatusOrdemServico.PAGO, dto.getStatus());
        assertEquals(200.0, dto.getValor());
    }

    @Test
    @DisplayName("PecaMapper converte ida e volta sem perder dados")
    void pecaIdaEVolta() {
        PecaRequestDTO request = new PecaRequestDTO();
        request.setCodigoBarras("123");
        request.setFornecedorMarca("Bosch");
        request.setQuantidadeEstoque(3);
        request.setPrecoCusto(10.0);
        request.setPrecoVenda(20.0);
        request.setTamanho("P");
        request.setCor("Azul");
        request.setCategoria(CategoriaPeca.ELETRICA);

        Peca peca = PecaMapper.paraModel(request);
        assertNull(peca.getCodigo(), "código é gerado pelo repositório, não pelo cliente");
        peca.setCodigo(1L);

        PecaResponseDTO response = PecaMapper.paraResponseDTO(peca);
        assertEquals(1L, response.getCodigo());
        assertEquals("123", response.getCodigoBarras());
        assertEquals("Bosch", response.getFornecedorMarca());
        assertEquals(3, response.getQuantidadeEstoque());
        assertEquals(10.0, response.getPrecoCusto());
        assertEquals(20.0, response.getPrecoVenda());
        assertEquals("P", response.getTamanho());
        assertEquals("Azul", response.getCor());
        assertEquals(CategoriaPeca.ELETRICA, response.getCategoria());
    }

    @Test
    @DisplayName("PedidoPecaMapper converte pedido com itens (entidade associativa) para DTO")
    void pedidoParaResponse() {
        Peca peca = new Peca();
        peca.setCodigo(5L);
        peca.setCodigoBarras("FILTRO");
        peca.setFornecedorMarca("Mann");
        peca.setPrecoVenda(30.0);

        PedidoPeca pedido = new PedidoPeca();
        pedido.setId(1L);
        ItemPedidoPeca item = new ItemPedidoPeca(peca, 2);
        item.setId(10L);
        pedido.adicionarItem(item);

        PedidoPecaResponseDTO dto = PedidoPecaMapper.paraResponseDTO(pedido);

        assertEquals(1L, dto.getId());
        assertEquals(1, dto.getItens().size());
        assertEquals(10L, dto.getItens().get(0).getId());
        assertEquals(5L, dto.getItens().get(0).getCodigoPeca());
        assertEquals("FILTRO", dto.getItens().get(0).getCodigoBarrasPeca());
        assertEquals("Mann", dto.getItens().get(0).getFornecedorMarcaPeca());
        assertEquals(60.0, dto.getItens().get(0).getSubtotal());
        assertEquals(60.0, dto.getValorTotal());
    }

    @Test
    @DisplayName("ServicoMapper.paraModel copia os campos do DTO")
    void servicoParaModel() {
        ServicoRequestDTO dto = new ServicoRequestDTO();
        dto.setDescricao("Alinhamento");
        dto.setValorMaoDeObra(80.0);
        dto.setCustoTabelado(100.0);
        dto.setTempoEstimadoMinutos(45.0);

        Servico servico = ServicoMapper.paraModel(dto);

        assertEquals("Alinhamento", servico.getDescricao());
        assertEquals(80.0, servico.getValorMaoDeObra());
        assertEquals(100.0, servico.getCustoTabelado());
        assertEquals(45.0, servico.getTempoEstimadoMinutos());
    }
}
