package br.com.mecaniqa.controller;

import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.StatusOrdemServico;
import br.com.mecaniqa.model.StatusPedidoPeca;
import br.com.mecaniqa.repository.OrdemServicoRepository;
import br.com.mecaniqa.repository.PecaRepository;
import br.com.mecaniqa.repository.PedidoPecaRepository;
import br.com.mecaniqa.service.OrdemServicoService;
import br.com.mecaniqa.service.PedidoPecaService;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Exercita HTTP, DTOs e services reais, isolando somente o acesso ao banco. */
class CrudOsPedidosEndpointTest {
    private OrdemServicoRepository ordens;
    private PedidoPecaRepository pedidos;
    private PecaRepository pecas;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        ordens = mock(OrdemServicoRepository.class);
        pedidos = mock(PedidoPecaRepository.class);
        pecas = mock(PecaRepository.class);
        mvc = MockMvcBuilders.standaloneSetup(
                new OrdemServicoController(new OrdemServicoService(ordens)),
                new PedidoPecaController(new PedidoPecaService(pedidos, pecas)))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(
                        Jackson2ObjectMapperBuilder.json()
                                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .build()))
                .build();
    }

    @Test
    void atualizarOsPreservaIdentidadeDataEStatus() throws Exception {
        OrdemServico ordem = OrdemServico.builder().id(7L)
                .descricaoProblema("Anterior").valor(10)
                .dataAbertura(LocalDate.of(2026, 8, 14))
                .status(StatusOrdemServico.EM_EXECUCAO).build();
        when(ordens.buscarPorId(7L)).thenReturn(Optional.of(ordem));
        when(ordens.update(any())).thenAnswer(invocacao -> invocacao.getArgument(0));

        mvc.perform(put("/api/ordens-servico/7").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"descricaoProblema":"Revisão","valor":250,
                                 "id":999,"dataAbertura":"2000-01-01","status":"PAGO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.descricaoProblema").value("Revisão"))
                .andExpect(jsonPath("$.valor").value(250.0))
                .andExpect(jsonPath("$.dataAbertura").value("2026-08-14"))
                .andExpect(jsonPath("$.status").value("EM_EXECUCAO"));
        verify(ordens).update(ordem);
        verify(ordens, never()).atualizarStatus(any(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"descricaoProblema\":\" \" ,\"valor\":10}",
            "{\"descricaoProblema\":\"Revisão\"}",
            "{\"descricaoProblema\":\"Revisão\",\"valor\":-1}",
            "{\"descricaoProblema\":\"Revisão\",\"valor\":\"NaN\"}", "{invalido"})
    void atualizarOsInvalidaRetorna400SemGravar(String json) throws Exception {
        mvc.perform(put("/api/ordens-servico/7").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(ordens);
    }

    @Test
    void atualizarOsInexistenteRetorna404() throws Exception {
        mvc.perform(put("/api/ordens-servico/99").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricaoProblema\":\"Revisão\",\"valor\":0}"))
                .andExpect(status().isNotFound());
        verify(ordens, never()).update(any());
    }

    @Test
    void excluirOsRetorna204SemCorpoE404NaRepeticao() throws Exception {
        when(ordens.existsById(7L)).thenReturn(true, false);
        mvc.perform(delete("/api/ordens-servico/7"))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(delete("/api/ordens-servico/7"))
                .andExpect(status().isNotFound());
        verify(ordens, times(1)).deleteById(7L);
    }

    @Test
    void excluirPedidoRetorna204SemCorpoE404NaRepeticao() throws Exception {
        when(pedidos.existsById(8L)).thenReturn(true, false);
        mvc.perform(delete("/api/pedidos/8"))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(delete("/api/pedidos/8"))
                .andExpect(status().isNotFound());
        verify(pedidos, times(1)).deleteById(8L);
        verify(pedidos, never()).findById(any());
    }

    @Test
    void rotasEspecificasDeStatusContinuamDisponiveis() throws Exception {
        OrdemServico ordem = OrdemServico.builder().id(7L)
                .status(StatusOrdemServico.PAGO).build();
        when(ordens.atualizarStatus(7L, StatusOrdemServico.PAGO))
                .thenReturn(Optional.of(ordem));
        mvc.perform(put("/api/ordens-servico/7/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PAGO\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAGO"));

        PedidoPeca pedido = new PedidoPeca();
        pedido.setId(8L);
        pedido.setStatus(StatusPedidoPeca.ENTREGUE);
        when(pedidos.atualizarStatus(8L, StatusPedidoPeca.ENTREGUE))
                .thenReturn(Optional.of(pedido));
        mvc.perform(put("/api/pedidos/8/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ENTREGUE\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ENTREGUE"));
    }

    @Test
    void adicionarPecaRepetidaPreservaItensEPrecoRegistrado() throws Exception {
        Peca peca = new Peca();
        peca.setCodigo(5L);
        peca.setPrecoVenda(10.0);
        PedidoPeca pedido = new PedidoPeca();
        pedido.setId(8L);
        pedido.adicionarItem(new ItemPedidoPeca(peca, 1));
        peca.setPrecoVenda(20.0);
        when(pecas.findById(5L)).thenReturn(Optional.of(peca));
        when(pedidos.adicionarItens(eq(8L), any())).thenAnswer(invocacao -> {
            List<ItemPedidoPeca> novos = invocacao.getArgument(1);
            novos.forEach(pedido::adicionarItem);
            return Optional.of(pedido);
        });

        mvc.perform(post("/api/pedidos/8/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\":[{\"codigoPeca\":5,\"quantidade\":2}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(2))
                .andExpect(jsonPath("$.itens[0].precoUnitario").value(10.0))
                .andExpect(jsonPath("$.itens[1].precoUnitario").value(20.0))
                .andExpect(jsonPath("$.valorTotal").value(50.0));
        verify(pedidos, never()).update(any());
    }

    @Test
    void valorZeroPermitido() {
        OrdemServico ordem = OrdemServico.builder().id(7L).build();
        when(ordens.buscarPorId(7L)).thenReturn(Optional.of(ordem));
        when(ordens.update(any())).thenReturn(ordem);
        assertTrue(new OrdemServicoService(ordens).atualizar(7L, "Diagnóstico", 0.0).isPresent());
        assertEquals(0.0, ordem.getValor());
    }
}
