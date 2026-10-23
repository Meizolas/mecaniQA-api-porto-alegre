package br.com.mecaniqa.suporte;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class LimpezaApi {

    public static final Recurso PEDIDOS = new Recurso("/api/pedidos", "id");
    public static final Recurso ORDENS = new Recurso("/api/ordens-servico", "id");
    public static final Recurso PECAS = new Recurso("/api/pecas", "codigo");

    public record Recurso(String url, String campoId) {
    }

    private final MockMvc mockMvc;
    private final Map<Recurso, Set<Long>> idsIniciais = new LinkedHashMap<>();

    public LimpezaApi(MockMvc mockMvc, Recurso... recursos) {
        this.mockMvc = mockMvc;
        for (Recurso recurso : recursos) {
            idsIniciais.put(recurso, new HashSet<>());
        }
    }

    public void registrarEstadoInicial() throws Exception {
        for (Map.Entry<Recurso, Set<Long>> entrada : idsIniciais.entrySet()) {
            entrada.getValue().clear();
            entrada.getValue().addAll(listarIds(entrada.getKey()));
        }
    }

    public void removerCriadosNoTeste() throws Exception {
        for (Map.Entry<Recurso, Set<Long>> entrada : idsIniciais.entrySet()) {
            Recurso recurso = entrada.getKey();
            for (Long id : listarIds(recurso)) {
                if (!entrada.getValue().contains(id)) {
                    mockMvc.perform(delete(recurso.url() + "/" + id));
                }
            }
        }
    }

    private Set<Long> listarIds(Recurso recurso) throws Exception {
        String corpo = mockMvc.perform(get(recurso.url()))
                .andReturn().getResponse().getContentAsString();
        List<Number> ids = JsonPath.read(corpo, "$[*]." + recurso.campoId());
        Set<Long> resultado = new HashSet<>();
        for (Number id : ids) {
            resultado.add(id.longValue());
        }
        return resultado;
    }
}

