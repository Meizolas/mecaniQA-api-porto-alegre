# Diagrama de Classes

## Entrega 1 — 14/08/2026 (versão original)

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

## Atualização — 29/08/2026 (com base no código atual)

Diagrama revisado para refletir o estado atual do código depois da entrega de hoje: nomes de atributos que mudaram em `Peca`/`Servico`, o valor `CARROCERIA` adicionado (e `FREIOS` renomeado para `FREIO`) em `CategoriaPeca`, a associação `Servico → Peca` (lista de peças utilizadas) e as novas classes `PecaRepository`/`ServicoRepository` (padrão Singleton).

**Principais diferenças em relação à versão de 14/08:**
- `Peca`: `codigo` → `id`, `fornecedorMarca` → `Fornecedor`, `precoVenda` → `preco`, `dataUltimaAtualizacao` → `dataAtualizacao`; getters/setters completos para todos os atributos.
- `Servico`: `nomeServico` → `descricao`; `tempoEstimadoMinutos` agora é `Double` (era `Integer` no diagrama); `dataUltimaAtualizacao` → `dataAtualizacao`; adicionado o atributo `pecasUtilazadas : List<Peca>` e o método `adicionarPeca(Peca)`.
- `CategoriaPeca`: `FREIOS` → `FREIO`; adicionado `CARROCERIA`.
- Novas classes `PecaRepository` e `ServicoRepository`, cada uma um Singleton (`getInstance()` estático) que guarda a lista de `Peca`/`Servico` em memória.

```mermaid
classDiagram
direction LR

class Peca {
  - id : Long
  - nome : String
  - codigoBarras : String
  - Fornecedor : String
  - preco : Double
  - quantidadeEstoque : Integer
  - categoria : CategoriaPeca
  - dataCadastro : LocalDateTime
  - dataAtualizacao : LocalDateTime
  + getId() Long
  + setId(Long id) void
  + getNome() String
  + setNome(String nome) void
  + getCodigoBarras() String
  + setCodigoBarras(String codigoBarras) void
  + getFornecedor() String
  + setFornecedor(String fornecedor) void
  + getPreco() Double
  + setPreco(Double preco) void
  + getQuantidadeEstoque() Integer
  + setQuantidadeEstoque(Integer qtd) void
  + getCategoria() CategoriaPeca
  + setCategoria(CategoriaPeca categoria) void
  + getDataCadastro() LocalDateTime
  + setDataCadastro(LocalDateTime data) void
  + getDataAtualizacao() LocalDateTime
  + setDataAtualizacao(LocalDateTime data) void
}

class Servico {
  - id : Long
  - descricao : String
  - valorMaoDeObra : Double
  - custoTabelado : Double
  - tempoEstimadoMinutos : Double
  - pecasUtilazadas : List~Peca~
  - dataCadastro : LocalDateTime
  - dataAtualizacao : LocalDateTime
  + getId() Long
  + setId(Long id) void
  + getDescricao() String
  + setDescricao(String descricao) void
  + getValorMaoDeObra() Double
  + setValorMaoDeObra(Double valor) void
  + getCustoTabelado() Double
  + setCustoTabelado(Double custo) void
  + gettempoEstimadoMinutos() Double
  + settempoEstimadoMinutos(Double tempo) void
  + getPecasUtilizadas() List~Peca~
  + setPecasUtilizadas(List~Peca~ pecas) void
  + adicionarPeca(Peca peca) void
  + getDataCadastro() LocalDateTime
  + setDataCadastro(LocalDateTime data) void
  + getDataAtualizacao() LocalDateTime
  + setDataAtualizacao(LocalDateTime data) void
}

class CategoriaPeca {
  <<enumeration>>
  MOTOR
  SUSPENSAO
  FREIO
  ELETRICA
  CARROCERIA
  ACESSORIOS
  GERAL
}

class PecaRepository {
  - instance$ : PecaRepository
  - pecas : List~Peca~
  - PecaRepository()
  + getInstance()$ PecaRepository
  + adicionarPeca(Peca peca) void
  + listarPecas() List~Peca~
}

class ServicoRepository {
  - instance$ : ServicoRepository
  - servicos : List~Servico~
  - ServicoRepository()
  + getInstance()$ ServicoRepository
  + adicionarServico(Servico servico) void
  + listarServicos() List~Servico~
}

Peca "1" --> "1" CategoriaPeca : categoria
Servico "1" o-- "0..*" Peca : pecasUtilazadas
PecaRepository "1" o-- "0..*" Peca : pecas
ServicoRepository "1" o-- "0..*" Servico : servicos
```
