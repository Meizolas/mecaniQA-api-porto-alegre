package br.com.mecaniqa.integracao;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.model.StatusOrdemServico;
import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de integração (HTTP -> Controller -> Mapper -> Builder -> Repository -> DTO)
 * para a Gestão de Ordem de Serviço: US01 (criar OS) e US02 (modificar status).
 *
 * Sobe o contexto Spring completo e dispara requisições reais via MockMvc,
 * validando verbos, status HTTP e o JSON devolvido (somente DTOs).
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integração - Ordem de Serviço (US01 e US02)")
class OrdemServicoApiIntegrationTest {

    private static final String URL = "/api/ordens-servico";

    @Autowired
    private MockMvc mockMvc;

    // ---------------------------------------------------------------- US01

    @Test
    @DisplayName("US01 - POST cria OS e retorna 201 com status ABERTO e data de abertura")
    void deveCriarOrdemDeServico() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricaoProblema\": \"Ruído no motor\", \"valor\": 350.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.descricaoProblema").value("Ruído no motor"))
                .andExpect(jsonPath("$.valor").value(350.0))
                .andExpect(jsonPath("$.status").value("ABERTO"))
                .andExpect(jsonPath("$.dataAbertura").value(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("US01 - Cenário QA: DTO ignora atributos internos enviados pelo cliente (id, status, data)")
    void deveIgnorarAtributosInternosEnviadosNoJson() throws Exception {
        String json = "{"
                + "\"id\": 999,"
                + "\"status\": \"PAGO\","
                + "\"dataAbertura\": \"2000-01-01\","
                + "\"descricaoProblema\": \"Troca de óleo\","
                + "\"valor\": 120.0"
                + "}";

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(not(999)))
                .andExpect(jsonPath("$.status").value("ABERTO"))
                .andExpect(jsonPath("$.dataAbertura").value(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("US01 - Sem valor informado a OS é criada com valor 0.0")
    void deveCriarOrdemComValorPadraoQuandoNaoInformado() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricaoProblema\": \"Diagnóstico\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(0.0));
    }

    @Test
    @DisplayName("US01 - Resposta expõe apenas os campos do OrdemServicoResponseDTO")
    void respostaDeveConterSomenteCamposDoDto() throws Exception {
        String body = criarOrdemERetornarJson("Freio fazendo barulho");

        java.util.Map<String, Object> campos = JsonPath.read(body, "$");
        org.hamcrest.MatcherAssert.assertThat(campos.keySet(),
                containsInAnyOrder("id", "descricaoProblema", "dataAbertura", "status", "valor"));
    }

    @Test
    @DisplayName("US01 - IDs gerados são sequenciais e não se repetem")
    void deveGerarIdsDiferentes() throws Exception {
        long id1 = idDe(criarOrdemERetornarJson("OS A"));
        long id2 = idDe(criarOrdemERetornarJson("OS B"));
        org.junit.jupiter.api.Assertions.assertTrue(id2 > id1);
    }

    @Test
    @DisplayName("US01 - JSON malformado retorna 400")
    void deveRetornar400ParaJsonMalformado() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{descricao: "))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------------- US02

    @ParameterizedTest(name = "US02 - PUT altera status para {0}")
    @EnumSource(StatusOrdemServico.class)
    void deveAtualizarParaTodosOsStatusObrigatorios(StatusOrdemServico novoStatus) throws Exception {
        long id = idDe(criarOrdemERetornarJson("Alinhamento"));

        mockMvc.perform(put(URL + "/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"" + novoStatus.name() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value(novoStatus.name()));
    }

    @Test
    @DisplayName("US02 - Status alterado fica persistido no repositório em memória (GET confirma)")
    void statusAlteradoDevePersistir() throws Exception {
        long id = idDe(criarOrdemERetornarJson("Revisão 10.000 km"));

        mockMvc.perform(put(URL + "/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"EM_EXECUCAO\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_EXECUCAO"));
    }

    @Test
    @DisplayName("US02 - Sem status no corpo retorna 400")
    void deveRetornar400SemStatus() throws Exception {
        long id = idDe(criarOrdemERetornarJson("Sem status"));

        mockMvc.perform(put(URL + "/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("US02 - Status fora do enum retorna 400")
    void deveRetornar400ParaStatusInvalido() throws Exception {
        long id = idDe(criarOrdemERetornarJson("Status inválido"));

        mockMvc.perform(put(URL + "/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"CANCELADO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("US02 - OS inexistente retorna 404")
    void deveRetornar404ParaOrdemInexistente() throws Exception {
        mockMvc.perform(put(URL + "/999999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PAGO\"}"))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------ consultas

    @Test
    @DisplayName("GET lista e GET por id retornam a OS criada; id inexistente retorna 404")
    void deveListarEBuscarPorId() throws Exception {
        long id = idDe(criarOrdemERetornarJson("Consulta"));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(hasItem((int) id)));

        mockMvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricaoProblema").value("Consulta"));

        mockMvc.perform(get(URL + "/999999"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------- helpers

    private String criarOrdemERetornarJson(String descricao) throws Exception {
        return mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricaoProblema\": \"" + descricao + "\", \"valor\": 100.0}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private long idDe(String json) {
        Number id = JsonPath.read(json, "$.id");
        return id.longValue();
    }
}
