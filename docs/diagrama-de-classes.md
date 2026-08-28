# Diagrama de Classes

Entrega 1, aula dia 14/08/2026

Diagrama de Classes com as entidades e o Enum preenchidos com atributos e modificadores de visibilidade.

Peca.java; 
Servico.java; 
E do Enum CategoriaPeca.java.


```mermaid
classDiagram
direction LR
class Peca {
  - codigo : Long
  - nome : String
  - codigoBarras : String
  - fornecedorMarca : String
  - quantidadeEstoque : Integer
  - precoVenda : Double
  - categoria : CategoriaPeca
  - dataCadastro : LocalDateTime
  - dataUltimaAtualizacao : LocalDateTime
  + getCodigo() Long
  + getNome() String
  + setNome(String nome) void
  + getQuantidadeEstoque() Integer
  + setQuantidadeEstoque(Integer qtd) void
  + getPrecoVenda() Double
  + setPrecoVenda(Double preco) void
}
class Servico {
  - id : Long
  - nomeServico : String
  - valorMaoDeObra : Double
  - tempoEstimadoMinutos : Integer
  - custoTabelado : Double
  - dataCadastro : LocalDateTime
  - dataUltimaAtualizacao : LocalDateTime
  + getId() Long
  + getNomeServico() String
  + setNomeServico(String nome) void
  + getValorMaoDeObra() Double
  + setValorMaoDeObra(Double valor) void
  + getCustoTabelado() Double
  + setCustoTabelado(Double custo) void
}
class CategoriaPeca {
  <<enumeration>>
  MOTOR
  SUSPENSAO
  FREIOS
  ELETRICA
  ACESSORIOS
  GERAL
}
Peca --> "1" CategoriaPeca : categoria
```
