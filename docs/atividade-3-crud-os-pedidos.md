# Atividade 3 — CRUDs de OS e Pedidos

Responsável: Albert.

## Rotas adicionadas

| Método e rota | Resultado |
| --- | --- |
| `PUT /api/ordens-servico/{id}` | 200 com a OS atualizada; 400 para dados inválidos; 404 se não existir |
| `DELETE /api/ordens-servico/{id}` | 204 sem corpo; 404 se não existir |
| `DELETE /api/pedidos/{id}` | 204 sem corpo; 404 se não existir |

O PUT usa o DTO existente, com `descricaoProblema` e `valor`. A descrição deve estar preenchida e o valor deve ser informado, finito e maior ou igual a zero. Exemplo:

```json
{
  "descricaoProblema": "Revisão do sistema de freios",
  "valor": 250.00
}
```

ID, data de abertura e status são preservados. O SQL de atualização altera apenas descrição e valor. A alteração de status continua em `PUT /api/ordens-servico/{id}/status`.

A exclusão de pedido usa apenas `DELETE FROM pedidos_pecas WHERE id = ?`. A chave estrangeira definida em `script_mecaniqa.sql` remove os itens por `ON DELETE CASCADE`. Não há exclusão manual de itens ou peças.

As rotas de criação, consulta, atualização de status e inclusão de itens permanecem com seus contratos existentes. A inclusão continua permitindo peças repetidas e preservando o preço registrado em cada item.

## Arquitetura

Controller → Service → Repository → ConsultaJdbcTemplate → MySQL.

Os controllers tratam os códigos HTTP manualmente; os services validam a atualização e verificam a existência antes de excluir. Os repositórios continuam implementando `Repository<T, ID>`. Não foram adicionados frameworks de persistência ou manipuladores globais de exceção.

## Testes sem banco

Os novos testes HTTP usam MockMvc com controllers e services reais e repositórios simulados. Os testes JDBC executam o template real com conexões simuladas, verificando o SQL e seus parâmetros.

```powershell
.\gradlew.bat test --tests '*CrudOsPedidosEndpointTest' --tests '*OrdemServicoCrudJdbcTest' --tests '*PedidoPecaRepositoryTransacaoTest'
```

Cobertura: atualização válida, dados inválidos, OS inexistente, preservação de ID/data/status, exclusão com 204 sem corpo, segunda exclusão com 404, rotas de status, inclusão de peça repetida e preço por item, SQL restrito da atualização de OS e exclusão somente do pedido.

Para os testes existentes que acessam MySQL, configure o banco local `mecaniqa` com `script_mecaniqa.sql` e o arquivo `.env` conforme `.env.example`. Execute em um banco de desenvolvimento: os testes criam e alteram registros.

```powershell
.\gradlew.bat test
```

Os testes com conexões simuladas não substituem a validação de persistência e de `ON DELETE CASCADE` em MySQL.

## Validação desta entrega

- Compilação de todas as classes de produção e de teste com Java 17 concluída.
- 49 testes aprovados: novos endpoints, JDBC de OS, transações/exclusão de pedidos, template JDBC, modelos e mapeadores.
- Os testes foram executados diretamente pelo JUnit Platform com as dependências do projeto e o agente Mockito carregado no início da JVM. A execução pelo Gradle encontrou bloqueio de cache e falha de anexação dinâmica do agente no ambiente Windows.
- Testes com MySQL não executados: não há `.env` nesta cópia e a porta local 3306 não respondeu.
