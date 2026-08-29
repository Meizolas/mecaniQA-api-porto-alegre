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

## Atualização intermediária — 29/08/2026

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
## Atualização final — 29/08/2026 (com base no código atual)

Diagrama revisado para refletir o estado atual do código depois das alterações realizadas até a entrega de hoje: atualização dos atributos das classes `Peca` e `Servico`, atualização dos valores do enum `CategoriaPeca`, associação entre `Servico` e `Peca` por meio da lista de peças utilizadas e inclusão das classes `PecaRepository` e `ServicoRepository`, implementadas utilizando o padrão Singleton.

**Principais diferenças em relação à versão anterior:**

- `Peca`: atualização dos atributos para refletir a implementação atual, incluindo `codigo`, `codigoBarras`, `fornecedorMarca`, `quantidadeEstoque`, `precoCusto`, `precoVenda`, `dataCadastro`, `dataUltimaAtualizacao`, `tamanho`, `cor` e `categoria`, além dos respectivos getters e setters.

- `Servico`: atualização do atributo `nomeServico` para `descricao`; `tempoEstimadoMinutos` passou a ser representado como `Double`; `dataUltimaAtualizacao` foi atualizada para `dataAtualizacao`; inclusão do atributo `pecasUtilizadas : List<Peca>` e do método `adicionarPeca(Peca)`.

- `CategoriaPeca`: atualização dos valores do enum de acordo com a implementação atual: `MOTOR`, `SUSPENSAO`, `FREIOS`, `ELETRICA` e `ACESSORIOS`.

- `Servico → Peca`: inclusão da associação entre `Servico` e `Peca`, representando a lista de peças utilizadas em cada serviço.

- `PecaRepository` e `ServicoRepository`: inclusão das classes responsáveis pelo armazenamento das peças e serviços em memória, respectivamente, utilizando o padrão Singleton por meio do método estático `getInstance()`, além das operações de cadastro, listagem, busca, atualização e exclusão.

```mermaid
classDiagram
direction LR

class Peca {
    -Long codigo
    -String codigoBarras
    -String fornecedorMarca
    -Integer quantidadeEstoque
    -Double precoCusto
    -Double precoVenda
    -LocalDateTime dataCadastro
    -LocalDateTime dataUltimaAtualizacao
    -String tamanho
    -String cor
    -CategoriaPeca categoria

    +Peca()
    +Peca(String codigoBarras, String fornecedorMarca, Integer quantidadeEstoque, Double precoCusto, Double precoVenda, String tamanho, String cor, CategoriaPeca categoria)
    +Long getCodigo()
    +void setCodigo(Long codigo)
    +String getCodigoBarras()
    +void setCodigoBarras(String codigoBarras)
    +String getFornecedorMarca()
    +void setFornecedorMarca(String fornecedorMarca)
    +Integer getQuantidadeEstoque()
    +void setQuantidadeEstoque(Integer quantidadeEstoque)
    +Double getPrecoCusto()
    +void setPrecoCusto(Double precoCusto)
    +Double getPrecoVenda()
    +void setPrecoVenda(Double precoVenda)
    +LocalDateTime getDataCadastro()
    +void setDataCadastro(LocalDateTime dataCadastro)
    +LocalDateTime getDataUltimaAtualizacao()
    +void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao)
    +String getTamanho()
    +void setTamanho(String tamanho)
    +String getCor()
    +void setCor(String cor)
    +CategoriaPeca getCategoria()
    +void setCategoria(CategoriaPeca categoria)
}

class Servico {
    -Long id
    -String descricao
    -Double valorMaoDeObra
    -Double custoTabelado
    -Double tempoEstimadoMinutos
    -List~Peca~ pecasUtilizadas
    -LocalDateTime dataCadastro
    -LocalDateTime dataAtualizacao

    +Servico()
    +Servico(String descricao, Double valorMaoDeObra, Double custoTabelado, Double tempoEstimadoMinutos, List~Peca~ pecasUtilizadas)
    +Long getId()
    +void setId(Long id)
    +String getDescricao()
    +void setDescricao(String descricao)
    +Double getValorMaoDeObra()
    +void setValorMaoDeObra(Double valorMaoDeObra)
    +Double getCustoTabelado()
    +void setCustoTabelado(Double custoTabelado)
    +Double getTempoEstimadoMinutos()
    +void setTempoEstimadoMinutos(Double tempoEstimadoMinutos)
    +List~Peca~ getPecasUtilizadas()
    +void setPecasUtilizadas(List~Peca~ pecasUtilizadas)
    +void adicionarPeca(Peca peca)
    +LocalDateTime getDataCadastro()
    +void setDataCadastro(LocalDateTime dataCadastro)
    +LocalDateTime getDataAtualizacao()
    +void setDataAtualizacao(LocalDateTime dataAtualizacao)
}

class CategoriaPeca {
    <<enumeration>>
    MOTOR
    SUSPENSAO
    FREIOS
    ELETRICA
    ACESSORIOS
}

class PecaRepository {
    -PecaRepository instance$
    -List~Peca~ pecas
    -Long proximoCodigo
    -PecaRepository()

    +PecaRepository getInstance()$
    +Peca salvar(Peca peca)
    +List~Peca~ listar()
    +Optional~Peca~ buscarPorCodigo(Long codigo)
    +Optional~Peca~ atualizar(Long codigo, Peca novosDados)
    +boolean excluir(Long codigo)
}

class ServicoRepository {
    -ServicoRepository instance$
    -List~Servico~ servicos
    -Long proximoId
    -ServicoRepository()

    +ServicoRepository getInstance()$
    +Servico salvar(Servico servico)
    +List~Servico~ listar()
    +Optional~Servico~ buscarPorId(Long id)
    +Optional~Servico~ atualizar(Long id, Servico novosDados)
    +boolean excluir(Long id)
}

class PecaController {
    -PecaRepository repository

    +PecaController()
    +ResponseEntity~Peca~ cadastrar(Peca peca)
    +ResponseEntity~List~Peca~~ listar()
    +ResponseEntity~Peca~ buscarPorCodigo(Long codigo)
    +ResponseEntity~Peca~ atualizar(Long codigo, Peca novosDados)
    +ResponseEntity~Void~ excluir(Long codigo)
}

class ServicoController {
    -ServicoRepository repository

    +ServicoController()
    +ResponseEntity~Servico~ cadastrar(Servico servico)
    +ResponseEntity~List~Servico~~ listar()
    +ResponseEntity~Servico~ buscarPorId(Long id)
    +ResponseEntity~Servico~ atualizar(Long id, Servico novosDados)
    +ResponseEntity~Void~ excluir(Long id)
}

Peca --> CategoriaPeca : categoria
Servico "1" o-- "0..*" Peca : pecasUtilizadas

PecaController --> PecaRepository : utiliza
ServicoController --> ServicoRepository : utiliza

PecaRepository "1" o-- "0..*" Peca : pecas
ServicoRepository "1" o-- "0..*" Servico : servicos
```
