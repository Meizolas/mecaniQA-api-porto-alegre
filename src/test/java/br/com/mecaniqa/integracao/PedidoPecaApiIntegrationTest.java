package br.com.mecaniqa.integracao;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mecaniqa.model.StatusPedidoPeca;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de integração da Gestão de Pedido de Peças:
 * US03 (criar pedido), US04 (adicionar peças via entidade associativa ItemPedidoPeca)
 * e US05 (modificar status do pedido).
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integração - Pedido de Peças (US03, US04 e US05)")
class PedidoPecaApiIntegrationTest {

    private static final String URL = "/api/pedidos";

    @Autowired
    private MockMvc mockMvc;

    private long pecaFiltro;   // preço de venda 35.0
    private long pecaPastilha; // preço de venda 80.0

    @BeforeEach
    void cadastrarPecasDeApoio() throws Exception {
        pecaFiltro = cadastrarPeca("FILTRO-OLEO", 35.0);
        pecaPastilha = cadastrarPeca("PASTILHA-FREIO", 80.0);
    }

    // ---------------------------------------------------------------- US03

    @Test
    @DisplayName("US03 - POST cria pedido vazio com 201, status ORCANDO e total 0")
    void deveCriarPedidoVazio() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{\"itens\": []}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("ORCANDO"))
                .andExpect(jsonPath("$.itens", hasSize(0)))
                .andExpect(jsonPath("$.valorTotal").value(0.0))
                .andExpect(jsonPath("$.dataCriacao").isNotEmpty())
                .andExpect(jsonPath("$.dataUltimaAtualizacao").isNotEmpty());
    }

    @Test
    @DisplayName("US03 - POST sem o campo itens também cria o pedido (DTO já inicia a lista)")
    void deveCriarPedidoSemCampoItens() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itens", hasSize(0)));
    }

    @Test
    @DisplayName("US03 - POST cria pedido já com várias peças e calcula subtotal e total")
    void deveCriarPedidoComItens() throws Exception {
        String json = "{\"itens\": ["
                + item(pecaFiltro, 2) + ","
                + item(pecaPastilha, 1)
                + "]}";

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itens", hasSize(2)))
                .andExpect(jsonPath("$.itens[0].codigoPeca").value(pecaFiltro))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2))
                .andExpect(jsonPath("$.itens[0].precoUnitario").value(35.0))
                .andExpect(jsonPath("$.itens[0].subtotal").value(70.0))
                .andExpect(jsonPath("$.itens[1].subtotal").value(80.0))
                .andExpect(jsonPath("$.valorTotal").value(150.0))
                .andExpect(jsonPath("$.itens[0].id").isNumber());
    }

    @Test
    @DisplayName("US03 - Cenário QA: status enviado pelo cliente é ignorado, pedido nasce ORCANDO")
    void deveIgnorarStatusEnviadoNaCriacao() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 777, \"status\": \"ENTREGUE\", \"itens\": []}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ORCANDO"));
    }

    @Test
    @DisplayName("US03 - Criar pedido com peça inexistente retorna 404")
    void deveRetornar404AoCriarComPecaInexistente() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(999999L, 1) + "]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("US03 - Criar pedido com quantidade zero retorna 400 com mensagem")
    void deveRetornar400AoCriarComQuantidadeInvalida() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 0) + "]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("quantidade maior que zero")));
    }

    // ---------------------------------------------------------------- US04

    @Test
    @DisplayName("US04 - Adiciona peça a pedido existente e atualiza total")
    void deveAdicionarPecaAoPedido() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 3) + "]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoId))
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].codigoPeca").value(pecaFiltro))
                .andExpect(jsonPath("$.itens[0].codigoBarrasPeca").value("FILTRO-OLEO"))
                .andExpect(jsonPath("$.itens[0].quantidade").value(3))
                .andExpect(jsonPath("$.itens[0].subtotal").value(105.0))
                .andExpect(jsonPath("$.valorTotal").value(105.0));
    }

    @Test
    @DisplayName("US04 - Cenário QA: a mesma peça pode ser adicionada várias vezes (entidade associativa)")
    void deveAdicionarMesmaPecaMaisDeUmaVez() throws Exception {
        long pedidoId = criarPedidoVazio();

        adicionar(pedidoId, item(pecaFiltro, 2));
        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 1) + "]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(2)))
                .andExpect(jsonPath("$.itens[0].codigoPeca").value(pecaFiltro))
                .andExpect(jsonPath("$.itens[1].codigoPeca").value(pecaFiltro))
                .andExpect(jsonPath("$.valorTotal").value(105.0));
    }

    @Test
    @DisplayName("US04 - Várias peças diferentes com quantidades diferentes na mesma requisição")
    void deveAdicionarPecasDiferentesComQuantidades() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 4) + "," + item(pecaPastilha, 2) + "]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(2)))
                .andExpect(jsonPath("$.itens[*].quantidade", hasItem(4)))
                .andExpect(jsonPath("$.itens[*].quantidade", hasItem(2)))
                .andExpect(jsonPath("$.valorTotal").value(4 * 35.0 + 2 * 80.0));
    }

    @Test
    @DisplayName("US04 - Item guarda o preço da peça no momento da inclusão (snapshot)")
    void itemDeveManterPrecoDoMomentoDaInclusao() throws Exception {
        long pedidoId = criarPedidoVazio();
        adicionar(pedidoId, item(pecaFiltro, 1));

        // muda o preço da peça depois de incluída no pedido
        mockMvc.perform(put("/api/pecas/" + pecaFiltro).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoBarras\": \"FILTRO-OLEO\", \"precoVenda\": 999.0}"))
                .andExpect(status().isOk());

        mockMvc.perform(get(URL + "/" + pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens[0].precoUnitario").value(35.0))
                .andExpect(jsonPath("$.valorTotal").value(35.0));
    }

    @Test
    @DisplayName("US04 - Pedido inexistente retorna 404")
    void deveRetornar404ParaPedidoInexistente() throws Exception {
        mockMvc.perform(post(URL + "/999999/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 1) + "]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("US04 - Peça inexistente retorna 404 e não altera o pedido")
    void deveRetornar404ParaPecaInexistente() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, 1) + "," + item(999999L, 1) + "]}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(URL + "/" + pedidoId))
                .andExpect(jsonPath("$.itens", hasSize(0)));
    }

    @ParameterizedTest(name = "US04 - quantidade {0} retorna 400")
    @ValueSource(ints = {0, -1, -50})
    void deveRetornar400ParaQuantidadeInvalida(int quantidade) throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + item(pecaFiltro, quantidade) + "]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("US04 - Lista de itens vazia, sem codigoPeca ou sem quantidade retorna 400")
    void deveRetornar400ParaItensIncompletos() throws Exception {
        long pedidoId = criarPedidoVazio();
        String url = URL + "/" + pedidoId + "/itens";

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content("{\"itens\": []}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [{\"quantidade\": 1}]}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [{\"codigoPeca\": " + pecaFiltro + "}]}"))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------------- US05

    @ParameterizedTest(name = "US05 - PUT altera status do pedido para {0}")
    @EnumSource(StatusPedidoPeca.class)
    void deveAtualizarParaTodosOsStatusObrigatorios(StatusPedidoPeca novoStatus) throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(put(URL + "/" + pedidoId + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"" + novoStatus.name() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(novoStatus.name()));

        mockMvc.perform(get(URL + "/" + pedidoId))
                .andExpect(jsonPath("$.status").value(novoStatus.name()));
    }

    @Test
    @DisplayName("US05 - Sem status retorna 400 com mensagem")
    void deveRetornar400SemStatus() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(put(URL + "/" + pedidoId + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("status")));
    }

    @Test
    @DisplayName("US05 - Status fora do enum retorna 400")
    void deveRetornar400ParaStatusInvalido() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(put(URL + "/" + pedidoId + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"CANCELADO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("US05 - Pedido inexistente retorna 404")
    void deveRetornar404AoAtualizarStatusDePedidoInexistente() throws Exception {
        mockMvc.perform(put(URL + "/999999/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"ENTREGUE\"}"))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------ consultas

    @Test
    @DisplayName("GET lista e GET por id; id inexistente retorna 404")
    void deveListarEBuscarPedido() throws Exception {
        long pedidoId = criarPedidoVazio();

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(hasItem((int) pedidoId)));
        mockMvc.perform(get(URL + "/" + pedidoId)).andExpect(status().isOk());
        mockMvc.perform(get(URL + "/999999")).andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------- helpers

    private long cadastrarPeca(String codigoBarras, double precoVenda) throws Exception {
        String json = "{\"codigoBarras\": \"" + codigoBarras + "\","
                + "\"fornecedorMarca\": \"MecaniQA\","
                + "\"quantidadeEstoque\": 10,"
                + "\"precoCusto\": 10.0,"
                + "\"precoVenda\": " + precoVenda + ","
                + "\"categoria\": \"MOTOR\"}";
        String resposta = mockMvc.perform(post("/api/pecas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Number codigo = JsonPath.read(resposta, "$.codigo");
        return codigo.longValue();
    }

    private long criarPedidoVazio() throws Exception {
        String resposta = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{\"itens\": []}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Number id = JsonPath.read(resposta, "$.id");
        return id.longValue();
    }

    private void adicionar(long pedidoId, String itemJson) throws Exception {
        mockMvc.perform(post(URL + "/" + pedidoId + "/itens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\": [" + itemJson + "]}"))
                .andExpect(status().isOk());
    }

    private static String item(long codigoPeca, int quantidade) {
        return "{\"codigoPeca\": " + codigoPeca + ", \"quantidade\": " + quantidade + "}";
    }
}
