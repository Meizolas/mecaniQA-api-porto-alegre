plugins {
    id("java")
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.mecaniqa"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("io.github.cdimascio:dotenv-java:3.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("com.mysql:mysql-connector-j")
}

// ".\gradlew test" roda somente testes unitarios (sem MySQL).
tasks.test {
    useJUnitPlatform {
        excludeTags("integracao")
    }
}

// ".\gradlew integrationTest" roda somente testes que precisam de MySQL.
// Exige um banco ISOLADO (nunca o banco principal da equipe), informado por:
//   MECANIQA_TEST_DB_URL, MECANIQA_TEST_DB_USER, MECANIQA_TEST_DB_PASSWORD
// Esses valores substituem DB_URL/DB_USER/DB_PASSWORD (do .env) apenas nesta tarefa.
tasks.register<Test>("integrationTest") {
    description = "Executa os testes de integracao contra o MySQL isolado."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("integracao")
    }
    shouldRunAfter(tasks.test)

    doFirst {
        val url = System.getenv("MECANIQA_TEST_DB_URL")
        val usuario = System.getenv("MECANIQA_TEST_DB_USER")
        val senha = System.getenv("MECANIQA_TEST_DB_PASSWORD")
        if (url.isNullOrBlank() || usuario.isNullOrBlank() || senha == null) {
            throw GradleException(
                "Defina MECANIQA_TEST_DB_URL, MECANIQA_TEST_DB_USER e " +
                    "MECANIQA_TEST_DB_PASSWORD apontando para o banco ISOLADO de testes."
            )
        }
        environment("DB_URL", url)
        environment("DB_USER", usuario)
        environment("DB_PASSWORD", senha)
        environment("MECANIQA_BANCO_ISOLADO", "true")
    }
}
