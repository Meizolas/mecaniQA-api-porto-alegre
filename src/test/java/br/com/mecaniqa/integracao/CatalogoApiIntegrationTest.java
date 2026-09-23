package br.com.mecaniqa.integracao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Regressão da OAT 1: garante que o CRUD de Peças e Serviços continua
 * funcionando depois da migração para DTOs/Mappers feita na OAT 2.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integração - Regressão CRUD de Peças e Serviços (OAT 1)")
class CatalogoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Peça: POST 201 -> GET 200 -> PUT 200 -> DELETE 204 -> GET 404")
    void cicloCompletoDePeca() throws Exception {
        String resposta = mockMvc.perform(post("/api/pecas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoBarras\": \"789000\", \"fornecedorMarca\": \"Bosch\","
                                + "\"quantidadeEstoque\": 5, \"precoCusto\": 20.0, \"precoVenda\": 40.0,"
                                + "\"tamanho\": \"M\", \"cor\": \"Preto\", \"categoria\": \"FREIOS\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").isNumber())
                .andExpect(jsonPath("$.categoria").value("FREIOS"))
                .andExpect(jsonPath("$.dataCadastro").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        long codigo = ((Number) JsonPath.read(resposta, "$.codigo")).longValue();

        mockMvc.perform(get("/api/pecas/" + codigo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedorMarca").value("Bosch"));

        mockMvc.perform(put("/api/pecas/" + codigo).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoBarras\": \"789000\", \"precoVenda\": 55.0, \"categoria\": \"FREIOS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(codigo))
                .andExpect(jsonPath("$.precoVenda").value(55.0));

        mockMvc.perform(delete("/api/pecas/" + codigo)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/pecas/" + codigo)).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/pecas/" + codigo)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Peça: categoria fora do enum retorna 400")
    void pecaComCategoriaInvalida() throws Exception {
        mockMvc.perform(post("/api/pecas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoBarras\": \"1\", \"categoria\": \"PNEU\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Serviço: POST 201 -> GET 200 -> PUT 200 -> DELETE 204 -> GET 404")
    void cicloCompletoDeServico() throws Exception {
        String resposta = mockMvc.perform(post("/api/servicos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\": \"Troca de pastilhas\", \"valorMaoDeObra\": 90.0,"
                                + "\"custoTabelado\": 150.0, \"tempoEstimadoMinutos\": 60}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.descricao").value("Troca de pastilhas"))
                .andReturn().getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(resposta, "$.id")).longValue();

        mockMvc.perform(get("/api/servicos/" + id)).andExpect(status().isOk());

        mockMvc.perform(put("/api/servicos/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\": \"Troca de pastilhas e discos\", \"valorMaoDeObra\": 120.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorMaoDeObra").value(120.0));

        mockMvc.perform(delete("/api/servicos/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/servicos/" + id)).andExpect(status().isNotFound());
    }
}
