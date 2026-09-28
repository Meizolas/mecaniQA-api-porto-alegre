# Diagrama de Atividade – US04

**Versão oficial, criada em 27/09/2026 conforme o código presente no projeto.**

- **User Story:** US04 – Adicionar peças a um pedido existente
- **Endpoint:** `POST /api/pedidos/{id}/itens`
- **Visão:** Controller – `PedidoPecaController.adicionarItens()`

![Diagrama de atividade da US04](diagrama-de-atividade.jpeg)

## Fluxo

1. O Controller recebe a requisição com a lista de itens (`AdicionarItensPedidoRequestDTO`).
2. Busca o pedido pelo id. Se não existir, responde **404 Not Found**.
3. Valida os itens: lista não vazia, código da peça informado e quantidade maior que zero. Se algum item for inválido, responde **400 Bad Request**.
4. Para cada item (laço representado por um nó de decisão/mesclagem):
   - busca a peça pelo código; se não existir, responde **404 Not Found**;
   - se existir, chama `PedidoPecaMapper.paraItemModel(dto, peca)` para converter o DTO na entidade associativa `ItemPedidoPeca`.
5. Adiciona os itens ao pedido (`PedidoPecaRepository.adicionarItens`). Se o retorno vier vazio, responde **404 Not Found**.
6. Converte sequencialmente o pedido em `PedidoPecaResponseDTO` e constrói a resposta **200 OK**.
7. Devolve a resposta ao cliente e encerra a atividade.

## Nós de controle UML

| Nó | Onde aparece |
|---|---|
| Início | Entrada da requisição |
| Decisão | pedido existe?, itens válidos?, há mais itens?, peça existe?, pedido atualizado? |
| Decisão/mesclagem | Controla a entrada e a repetição do laço que percorre os itens |
| Final de fluxo | Encerra cada caminho de erro (400 / 404) |
| Final da atividade | Encerra o caminho de sucesso |
