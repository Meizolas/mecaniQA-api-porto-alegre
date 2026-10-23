package br.com.mecaniqa.suporte;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("integracao")
@EnabledIfEnvironmentVariable(
        named = "MECANIQA_BANCO_ISOLADO",
        matches = "true",
        disabledReason = "Teste de integracao: requer o banco MySQL isolado de testes.")
public @interface TesteIntegracao {
}

