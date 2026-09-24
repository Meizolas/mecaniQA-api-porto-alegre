package br.com.mecaniqa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Valida a estrutura do Padrão Builder exigida pela OAT 2 / barema:
 * classe estática aninhada, encadeamento com "return this" e
 * construtor privado (impede "new OrdemServico()").
 */
@DisplayName("Padrão Builder - OrdemServico")
class OrdemServicoBuilderTest {

    @Test
    @DisplayName("Todos os construtores de OrdemServico são privados (new OrdemServico() não compila)")
    void construtoresDevemSerPrivados() {
        Constructor<?>[] construtores = OrdemServico.class.getDeclaredConstructors();

        assertEquals(1, construtores.length, "deve existir um único construtor");
        assertTrue(Modifier.isPrivate(construtores[0].getModifiers()), "o construtor deve ser private");
        assertEquals(1, construtores[0].getParameterCount());
        assertEquals(OrdemServico.Builder.class, construtores[0].getParameterTypes()[0],
                "o construtor deve receber apenas o Builder");
    }

    @Test
    @DisplayName("Builder é uma classe public static aninhada em OrdemServico")
    void builderDeveSerClasseEstaticaAninhada() {
        Class<?> builder = OrdemServico.Builder.class;

        assertEquals(OrdemServico.class, builder.getEnclosingClass());
        assertTrue(Modifier.isStatic(builder.getModifiers()));
        assertTrue(Modifier.isPublic(builder.getModifiers()));
    }

    @Test
    @DisplayName("Cada método do Builder retorna a própria instância (return this)")
    void metodosDevemRetornarAMesmaInstancia() {
        OrdemServico.Builder builder = OrdemServico.builder();

        assertSame(builder, builder.id(1L));
        assertSame(builder, builder.descricaoProblema("Teste"));
        assertSame(builder, builder.dataAbertura(LocalDate.now()));
        assertSame(builder, builder.status(StatusOrdemServico.ABERTO));
        assertSame(builder, builder.valor(10.0));
    }

    @Test
    @DisplayName("build() copia todos os atributos configurados no encadeamento")
    void buildDeveCopiarTodosOsAtributos() {
        LocalDate data = LocalDate.of(2026, 9, 18);

        OrdemServico ordem = OrdemServico.builder()
                .id(42L)
                .descricaoProblema("Barulho na suspensão")
                .dataAbertura(data)
                .status(StatusOrdemServico.PENDENTE_PAGAMENTO)
                .valor(480.5)
                .build();

        assertEquals(42L, ordem.getId());
        assertEquals("Barulho na suspensão", ordem.getDescricaoProblema());
        assertEquals(data, ordem.getDataAbertura());
        assertEquals(StatusOrdemServico.PENDENTE_PAGAMENTO, ordem.getStatus());
        assertEquals(480.5, ordem.getValor());
    }

    @Test
    @DisplayName("Cada chamada de build() gera um objeto novo")
    void buildDeveGerarObjetosDistintos() {
        OrdemServico.Builder builder = OrdemServico.builder().descricaoProblema("X");

        assertNotSame(builder.build(), builder.build());
    }

    @Test
    @DisplayName("Enum StatusOrdemServico possui exatamente os 5 status obrigatórios")
    void statusObrigatoriosDaOrdem() {
        assertEquals(5, StatusOrdemServico.values().length);
        StatusOrdemServico.valueOf("ABERTO");
        StatusOrdemServico.valueOf("PENDENTE_PAGAMENTO");
        StatusOrdemServico.valueOf("PAGO");
        StatusOrdemServico.valueOf("EM_EXECUCAO");
        StatusOrdemServico.valueOf("EXECUTADO");
    }
}
