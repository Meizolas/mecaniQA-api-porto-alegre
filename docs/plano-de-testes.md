# Plano de Testes – OAT 2 (Padrão de Projeto e Diagrama de Atividade)

Responsável: **Marcelo Henrique Fernandes** – Testes, integração e Postman

## 1. Como executar

**Testes automatizados (JUnit 5 + MockMvc)**

```bash
./gradlew test          # Linux/macOS
gradlew.bat test        # Windows
```

Relatório HTML: `build/reports/tests/test/index.html`

**Postman**

1. Suba a API: `gradlew.bat bootRun` (porta 8080).
2. Importe `postman/MecaniQA-OAT2.postman_collection.json`.
3. Rode a collection inteira no **Collection Runner**, na ordem das pastas.
   Os IDs (peças, OS, pedido) são salvos sozinhos nas variáveis da collection.

Linha de comando (opcional): `npx newman run postman/MecaniQA-OAT2.postman_collection.json`

> A persistência é em memória (Singleton). Se reiniciar a API, rode a pasta **00 - Preparação** de novo.

## 2. Estratégia

| Nível | O que valida | Arquivos |
|---|---|---|
| Unitário | Builder, entidade associativa, mappers, enums de status | `model/OrdemServicoBuilderTest`, `model/EntidadeAssociativaTest`, `mapper/MapperTest` |
| Controller (unitário) | Controllers chamados direto em Java (já existiam) | `controller/*ControllerTest` |
| Integração | Requisição HTTP real → Controller → Mapper → Builder → Repository → DTO em JSON | `integracao/OrdemServicoApiIntegrationTest`, `integracao/PedidoPecaApiIntegrationTest`, `integracao/CatalogoApiIntegrationTest` |
| Manual/API | Fluxo completo com asserts automáticos no Postman | `postman/MecaniQA-OAT2.postman_collection.json` |

## 3. Matriz de cobertura (User Story × cenário)

| US | Endpoint | Sucesso | Erros cobertos |
|---|---|---|---|
| US01 Criar OS | `POST /api/ordens-servico` | 201, status `ABERTO`, id e `dataAbertura` gerados, `valor` padrão 0.0 | 400 JSON malformado |
| US02 Status da OS | `PUT /api/ordens-servico/{id}/status` | 200 para os 5 status (`ABERTO`, `PENDENTE_PAGAMENTO`, `PAGO`, `EM_EXECUCAO`, `EXECUTADO`) e GET confirma | 400 sem status, 400 status inválido, 404 OS inexistente |
| US03 Criar Pedido | `POST /api/pedidos` | 201 vazio (`ORCANDO`), 201 com itens + subtotal/total | 404 peça inexistente, 400 quantidade 0 |
| US04 Adicionar peças | `POST /api/pedidos/{id}/itens` | 200, subtotal e total, várias peças diferentes, **mesma peça mais de uma vez** | 404 pedido, 404 peça (pedido não é alterado), 400 quantidade 0/negativa, lista vazia, item sem `codigoPeca`/`quantidade` |
| US05 Status do Pedido | `PUT /api/pedidos/{id}/status` | 200 para os 4 status (`ORCANDO`, `PENDENTE_PAGAMENTO`, `PAGO_FATURADO`, `ENTREGUE`) e GET confirma | 400 sem status (com mensagem), 400 status inválido, 404 pedido inexistente |
| Regressão OAT 1 | `/api/pecas`, `/api/servicos` | CRUD completo (201/200/204) | 404 após exclusão, 400 categoria inválida |

## 4. Cenários do QA (Caderno de Guias)

| Encontro | Cenário | Onde é validado |
|---|---|---|
| 04/09 | Adicionar a mesma peça várias vezes a um pedido pela entidade associativa | `PedidoPecaApiIntegrationTest.deveAdicionarMesmaPecaMaisDeUmaVez`, `EntidadeAssociativaTest`, Postman "Adicionar a MESMA peça A de novo" |
| 11/09 | Não deve ser possível `new OrdemServico()` – só via `OrdemServico.builder()` | `OrdemServicoBuilderTest.construtoresDevemSerPrivados` (construtor único, `private`, recebe só o `Builder`) |
| 18/09 | O sistema não quebra se o cliente não enviar (ou enviar) atributos internos – o DTO restringe a carga | `OrdemServicoApiIntegrationTest.deveIgnorarAtributosInternosEnviadosNoJson`, `PedidoPecaApiIntegrationTest.deveIgnorarStatusEnviadoNaCriacao`, Postman "DTO ignora atributos internos" |

## 5. Ligação com o barema

| Item do barema (Código) | Teste que comprova |
|---|---|
| Endpoints US01–US05 | Pastas US01–US05 do Postman + classes de integração |
| Builder: classe estática aninhada, `return this`, construtor privado | `OrdemServicoBuilderTest` |
| DTO: transfere só os dados necessários | Teste "Resposta expõe apenas os campos do OrdemServicoResponseDTO" + cenários de atributos internos |
| Mappers como classes utilitárias | `MapperTest` (métodos públicos são `static`; conversão ida e volta) |
| Entidade associativa Peça × Pedido | `EntidadeAssociativaTest` + US04 |

## 6. Observações encontradas durante os testes

- `PecaMapper` e `PedidoPecaMapper` não têm construtor privado nem `final` (os outros dois mappers têm). Sugestão para ficar igual ao barema de "classe utilitária": `public final class PecaMapper { private PecaMapper() {} ... }`.
- US04 fala em adicionar peças "a um pedido já aberto", mas hoje o endpoint aceita adicionar itens mesmo com o pedido `ENTREGUE`. Se a equipe quiser, dá para devolver 400/409 quando o status não for `ORCANDO`.
- O item do pedido guarda o preço da peça no momento da inclusão (`precoUnitario`). Mudanças posteriores no preço da peça não alteram pedidos já montados – comportamento testado e desejado.
