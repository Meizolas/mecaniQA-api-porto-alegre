# MecaniQA API

API REST para gerenciamento de uma oficina mecânica. O sistema permite cadastrar peças e serviços, controlar ordens de serviço e registrar pedidos de peças com seus respectivos itens.

Os dados são persistidos em um banco **MySQL** por meio de **JDBC**, seguindo uma organização em camadas (`controller`, `service`, `repository`, `mapper`, `dto` e `model`).

## Tecnologias

- Java 17
- Spring Boot 3.5
- Gradle
- MySQL
- JDBC
- JUnit 5

## Como executar

### Pré-requisitos

- Java 17 instalado
- MySQL em execução

### Configuração

1. Clone o projeto e acesse sua pasta.
2. Execute o arquivo `script_mecaniqa.sql` no MySQL. Ele cria o banco `mecaniqa` e suas tabelas.
3. Copie o arquivo `.env.example` para um novo arquivo chamado `.env`.
4. Preencha o `.env` com os dados da sua conexão:

```env
DB_URL=jdbc:mysql://localhost:3306/mecaniqa
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

5. Inicie a aplicação:

```powershell
.\gradlew.bat bootRun
```

No Linux ou macOS, use `./gradlew bootRun`. A API ficará disponível, por padrão, em `http://localhost:8080`.

## Principais rotas

| Recurso | Rota base | Operações |
|---|---|---|
| Peças | `/api/pecas` | cadastrar, listar, consultar, atualizar e excluir |
| Serviços | `/api/servicos` | cadastrar, listar, consultar, atualizar e excluir |
| Ordens de serviço | `/api/ordens-servico` | CRUD e atualização de status |
| Pedidos de peças | `/api/pedidos` | criar, listar, consultar, excluir, adicionar itens e atualizar status |

As coleções disponíveis na pasta `postman/` podem ser importadas no Postman para testar os endpoints.

## Estrutura do projeto

```text
src/main/java/br/com/mecaniqa/
├── config/       # conexão com o MySQL
├── controller/   # endpoints REST
├── dto/          # dados de entrada e saída da API
├── mapper/       # conversão entre DTOs, modelos e resultados SQL
├── model/        # entidades e regras do domínio
├── repository/   # persistência com JDBC
└── service/      # regras de negócio
```

## Testes

Para executar os testes unitários, sem depender do MySQL:

```powershell
.\gradlew.bat test
```

Os testes de integração exigem um banco MySQL isolado. Configure as variáveis `MECANIQA_TEST_DB_URL`, `MECANIQA_TEST_DB_USER` e `MECANIQA_TEST_DB_PASSWORD` e execute:

```powershell
.\gradlew.bat integrationTest
```

> Nunca utilize o banco principal da equipe nos testes de integração, pois eles criam e removem dados.

## Equipe

- Hélio Nunes Moraes Junior
- Kevilyn Bitencourt da Cruz
- Rodrigo Alves Oliveira
- Marcelo Henrique Fernandes
- Murillo Rigaud dos Santos Oliveira
- Albert Santos Soares
