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
## Atualização — 29/08/2026 (com base no código atual)

Diagrama revisado para refletir o estado atual do código depois das alterações realizadas até a entrega de hoje: atualização dos atributos das classes `Peca` e `Servico`, atualização dos valores do enum `CategoriaPeca`, associação entre `Servico` e `Peca` por meio da lista de peças utilizadas e inclusão das classes `PecaRepository` e `ServicoRepository`, implementadas utilizando o padrão Singleton.

**Principais diferenças em relação à versão anterior:**

- `Peca`: atualização dos atributos para refletir a implementação atual, incluindo `id`, `Fornecedor`, `quantidadeEstoque`, `precoCusto`, `preco`, `dataCadastro`, `dataAtualizacao`, `tamanho`, `cor` e `categoria`, além dos respectivos getters e setters.

- `Servico`: atualização do atributo `nomeServico` para `descricao`; `tempoEstimadoMinutos` passou a ser representado como `Double`; `dataUltimaAtualizacao` foi atualizada para `dataAtualizacao`; inclusão do atributo `pecasUtilazadas : List<Peca>` e do método `adicionarPeca(Peca)`.

- `CategoriaPeca`: atualização dos valores do enum de acordo com a implementação atual, incluindo `MOTOR`, `SUSPENSAO`, `FREIO`, `ELETRICA`, `ACESSORIOS` e `CARROCERIA`.

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
    -List~Peca~ pecasUtilazadas
    -LocalDateTime dataCadastro
    -LocalDateTime dataAtualizacao

    +Servico()
    +Servico(Long id, String descricao, Double valorMaoDeObra, Double custoTabelado, Double tempoEstimadoMinutos, List~Peca~ pecasUtilazadas, LocalDateTime dataCadastro, LocalDateTime dataAtualizacao)
    +Long getId()
    +void setId(Long id)
    +String getDescricao()
    +void setDescricao(String descricao)
    +Double getValorMaoDeObra()
    +void setValorMaoDeObra(Double valorMaoDeObra)
    +Double getCustoTabelado()
    +void setCustoTabelado(Double custoTabelado)
    +Double gettempoEstimadoMinutos()
    +void settempoEstimadoMinutos(Double tempoEstimadoMinutos)
    +List~Peca~ getPecasUtilizadas()
    +void setPecasUtilizadas(List~Peca~ pecasUtilazadas)
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
Servico "1" o-- "0..*" Peca : pecasUtilazadas

PecaController --> PecaRepository : utiliza
ServicoController --> ServicoRepository : utiliza

PecaRepository "1" o-- "0..*" Peca : pecas
ServicoRepository "1" o-- "0..*" Servico : servicos


class OrdemServico {
    -Long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    - OrdemServico(builder : Builder)
    + builder()$ : Builder
    + getId() : Long
    + setId(id : Long) : void
    + getDescricaoProblema() : String
    + setDescricaoProblema(descricaoProblema : String) : void
    + getDataAbertura() : LocalDate
    + setDataAbertura(dataAbertura : LocalDate) : void
    + getStatus() : StatusOrdemServico
    + setStatus(status : StatusOrdemServico) : void
    + getValor() : double
    + setValor(valor : double) : void

    }

    class Builder {
    -long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    + id(id : long) : Builder
    + descricaoProblema(descricaoProblema : String) : Builder
    + dataAbertura(dataAbertura : LocalDate) : Builder
    + status(status : StatusOrdemServico) : Builder
    + valor(valor : double) : Builder
    + build() : OrdemServico
}

Builder ..> OrdemServico : cria

note for Builder "Classe interna pública e estática de OrdemServico"

---

# Diagrama de Classes

Versão atualizada em 20/09/2026 conforme o código presente no projeto.

```mermaid
classDiagram

direction LR

%% =========================================================
%% ENUMERAÇÕES
%% =========================================================

class CategoriaPeca {
    <<enumeration>>
    MOTOR
    SUSPENSAO
    FREIOS
    ELETRICA
    ACESSORIOS
}

class StatusOrdemServico {
    <<enumeration>>
    ABERTO
    PENDENTE_PAGAMENTO
    PAGO
    EM_EXECUCAO
    EXECUTADO
}

class StatusPedidoPeca {
    <<enumeration>>
    ORCANDO
    PENDENTE_PAGAMENTO
    PAGO_FATURADO
    ENTREGUE
}

%% =========================================================
%% MODELOS
%% =========================================================

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

class ItemPedidoPeca {
    -Long id
    -Peca peca
    -Integer quantidade
    -Double precoUnitario

    +ItemPedidoPeca()
    +ItemPedidoPeca(Peca peca, Integer quantidade)
    +Long getId()
    +void setId(Long id)
    +Peca getPeca()
    +void setPeca(Peca peca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
    +Double getPrecoUnitario()
    +void setPrecoUnitario(Double precoUnitario)
    +Double getSubtotal()
}

class PedidoPeca {
    -Long id
    -StatusPedidoPeca status
    -LocalDateTime dataCriacao
    -LocalDateTime dataUltimaAtualizacao
    -List~ItemPedidoPeca~ itens

    +PedidoPeca()
    +Long getId()
    +void setId(Long id)
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
    +LocalDateTime getDataCriacao()
    +void setDataCriacao(LocalDateTime dataCriacao)
    +LocalDateTime getDataUltimaAtualizacao()
    +void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao)
    +List~ItemPedidoPeca~ getItens()
    +void setItens(List~ItemPedidoPeca~ itens)
    +void adicionarItem(ItemPedidoPeca item)
    +Double getValorTotal()
}

class OrdemServico {
    -Long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    -OrdemServico(Builder builder)
    +builder()$ : Builder
    +Long getId()
    +void setId(Long id)
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +LocalDate getDataAbertura()
    +void setDataAbertura(LocalDate dataAbertura)
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
    +double getValor()
    +void setValor(double valor)
}

class Builder {
    <<static nested class>>
    -long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    +id(long id) : Builder
    +descricaoProblema(String descricaoProblema) : Builder
    +dataAbertura(LocalDate dataAbertura) : Builder
    +status(StatusOrdemServico status) : Builder
    +valor(double valor) : Builder
    +build() : OrdemServico
}

%% =========================================================
%% DTOs - PEÇA
%% =========================================================

class PecaRequestDTO {
    -String codigoBarras
    -String fornecedorMarca
    -Integer quantidadeEstoque
    -Double precoCusto
    -Double precoVenda
    -String tamanho
    -String cor
    -CategoriaPeca categoria

    +PecaRequestDTO()
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
    +String getTamanho()
    +void setTamanho(String tamanho)
    +String getCor()
    +void setCor(String cor)
    +CategoriaPeca getCategoria()
    +void setCategoria(CategoriaPeca categoria)
}

class PecaResponseDTO {
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

    +PecaResponseDTO()
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

%% =========================================================
%% DTOs - SERVIÇO
%% =========================================================

class ServicoRequestDTO {
    -String descricao
    -Double valorMaoDeObra
    -Double custoTabelado
    -Double tempoEstimadoMinutos

    +ServicoRequestDTO()
    +String getDescricao()
    +void setDescricao(String descricao)
    +Double getValorMaoDeObra()
    +void setValorMaoDeObra(Double valorMaoDeObra)
    +Double getCustoTabelado()
    +void setCustoTabelado(Double custoTabelado)
    +Double getTempoEstimadoMinutos()
    +void setTempoEstimadoMinutos(Double tempoEstimadoMinutos)
}

class ServicoResponseDTO {
    -Long id
    -String descricao
    -Double valorMaoDeObra
    -Double custoTabelado
    -Double tempoEstimadoMinutos
    -LocalDateTime dataCadastro
    -LocalDateTime dataAtualizacao

    +ServicoResponseDTO()
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
    +LocalDateTime getDataCadastro()
    +void setDataCadastro(LocalDateTime dataCadastro)
    +LocalDateTime getDataAtualizacao()
    +void setDataAtualizacao(LocalDateTime dataAtualizacao)
}

%% =========================================================
%% DTOs - PEDIDO DE PEÇAS
%% =========================================================

class ItemPedidoPecaRequestDTO {
    -Long codigoPeca
    -Integer quantidade

    +ItemPedidoPecaRequestDTO()
    +Long getCodigoPeca()
    +void setCodigoPeca(Long codigoPeca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
}

class ItemPedidoPecaResponseDTO {
    -Long id
    -Long codigoPeca
    -String codigoBarrasPeca
    -String fornecedorMarcaPeca
    -Integer quantidade
    -Double precoUnitario
    -Double subtotal

    +ItemPedidoPecaResponseDTO()
    +Long getId()
    +void setId(Long id)
    +Long getCodigoPeca()
    +void setCodigoPeca(Long codigoPeca)
    +String getCodigoBarrasPeca()
    +void setCodigoBarrasPeca(String codigoBarrasPeca)
    +String getFornecedorMarcaPeca()
    +void setFornecedorMarcaPeca(String fornecedorMarcaPeca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
    +Double getPrecoUnitario()
    +void setPrecoUnitario(Double precoUnitario)
    +Double getSubtotal()
    +void setSubtotal(Double subtotal)
}

class PedidoPecaRequestDTO {
    -List~ItemPedidoPecaRequestDTO~ itens

    +PedidoPecaRequestDTO()
    +List~ItemPedidoPecaRequestDTO~ getItens()
    +void setItens(List~ItemPedidoPecaRequestDTO~ itens)
}

class AdicionarItensPedidoRequestDTO {
    -List~ItemPedidoPecaRequestDTO~ itens

    +AdicionarItensPedidoRequestDTO()
    +List~ItemPedidoPecaRequestDTO~ getItens()
    +void setItens(List~ItemPedidoPecaRequestDTO~ itens)
}

class AtualizarStatusPedidoRequestDTO {
    -StatusPedidoPeca status

    +AtualizarStatusPedidoRequestDTO()
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
}

class PedidoPecaResponseDTO {
    -Long id
    -StatusPedidoPeca status
    -LocalDateTime dataCriacao
    -LocalDateTime dataUltimaAtualizacao
    -List~ItemPedidoPecaResponseDTO~ itens
    -Double valorTotal

    +PedidoPecaResponseDTO()
    +Long getId()
    +void setId(Long id)
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
    +LocalDateTime getDataCriacao()
    +void setDataCriacao(LocalDateTime dataCriacao)
    +LocalDateTime getDataUltimaAtualizacao()
    +void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao)
    +List~ItemPedidoPecaResponseDTO~ getItens()
    +void setItens(List~ItemPedidoPecaResponseDTO~ itens)
    +Double getValorTotal()
    +void setValorTotal(Double valorTotal)
}

%% =========================================================
%% DTOs - ORDEM DE SERVIÇO
%% =========================================================

class OrdemServicoRequestDTO {
    -String descricaoProblema
    -Double valor

    +OrdemServicoRequestDTO()
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +Double getValor()
    +void setValor(Double valor)
}

class OrdemServicoResponseDTO {
    -Long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -Double valor

    +OrdemServicoResponseDTO()
    +Long getId()
    +void setId(Long id)
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +LocalDate getDataAbertura()
    +void setDataAbertura(LocalDate dataAbertura)
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
    +Double getValor()
    +void setValor(Double valor)
}

class AtualizarStatusOrdemDTO {
    -StatusOrdemServico status

    +AtualizarStatusOrdemDTO()
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
}

%% =========================================================
%% MAPPERS
%% =========================================================

class PecaMapper {
    +Peca paraModel(PecaRequestDTO dto)$
    +PecaResponseDTO paraResponseDTO(Peca peca)$
}

class ServicoMapper {
    <<final>>
    -ServicoMapper()
    +Servico paraModel(ServicoRequestDTO dto)$
    +ServicoResponseDTO paraResponseDTO(Servico servico)$
}

class PedidoPecaMapper {
    +PedidoPeca paraModel(PedidoPecaRequestDTO dto)$
    +ItemPedidoPecaResponseDTO paraItemResponseDTO(ItemPedidoPeca item)$
    +PedidoPecaResponseDTO paraResponseDTO(PedidoPeca pedido)$
}

class OrdemServicoMapper {
    <<final>>
    -OrdemServicoMapper()
    +OrdemServico paraModel(OrdemServicoRequestDTO dto)$
    +OrdemServicoResponseDTO paraResponseDTO(OrdemServico ordem)$
}

%% =========================================================
%% REPOSITÓRIOS
%% =========================================================

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

class PedidoPecaRepository {
    -PedidoPecaRepository instance$
    -List~PedidoPeca~ pedidos
    -Long proximoIdPedido
    -Long proximoIdItem

    -PedidoPecaRepository()
    +PedidoPecaRepository getInstance()$
    +PedidoPeca salvar(PedidoPeca pedido)
    +PedidoPeca adicionarItens(Long idPedido, List~ItemPedidoPeca~ itens)
    +Optional~PedidoPeca~ atualizarStatus(Long idPedido, StatusPedidoPeca status)
    +List~PedidoPeca~ listar()
    +Optional~PedidoPeca~ buscarPorId(Long id)
}

class OrdemServicoRepository {
    -OrdemServicoRepository instance$
    -List~OrdemServico~ ordens
    -long proximoId

    -OrdemServicoRepository()
    +OrdemServicoRepository getInstance()$
    +OrdemServico salvar(OrdemServico ordem)
    +List~OrdemServico~ listar()
    +Optional~OrdemServico~ buscarPorId(Long id)
    +Optional~OrdemServico~ atualizarStatus(Long id, StatusOrdemServico status)
}

%% =========================================================
%% CONTROLLERS
%% =========================================================

class PecaController {
    -PecaRepository repository

    +PecaController()
    +ResponseEntity~PecaResponseDTO~ cadastrar(PecaRequestDTO dto)
    +ResponseEntity~List~PecaResponseDTO~~ listar()
    +ResponseEntity~PecaResponseDTO~ buscarPorCodigo(Long codigo)
    +ResponseEntity~PecaResponseDTO~ atualizar(Long codigo, PecaRequestDTO dto)
    +ResponseEntity~Void~ excluir(Long codigo)
}

class ServicoController {
    -ServicoRepository repository

    +ServicoController()
    +ResponseEntity~ServicoResponseDTO~ cadastrar(ServicoRequestDTO dto)
    +ResponseEntity~List~ServicoResponseDTO~~ listar()
    +ResponseEntity~ServicoResponseDTO~ buscarPorId(Long id)
    +ResponseEntity~ServicoResponseDTO~ atualizar(Long id, ServicoRequestDTO dto)
    +ResponseEntity~Void~ excluir(Long id)
}

class PedidoPecaController {
    -PedidoPecaRepository pedidoRepository
    -PecaRepository pecaRepository

    +PedidoPecaController()
    +ResponseEntity~PedidoPecaResponseDTO~ criarPedido(PedidoPecaRequestDTO dto)
    +ResponseEntity~PedidoPecaResponseDTO~ adicionarItens(Long idPedido, AdicionarItensPedidoRequestDTO dto)
    +ResponseEntity~PedidoPecaResponseDTO~ atualizarStatus(Long idPedido, AtualizarStatusPedidoRequestDTO dto)
    +ResponseEntity~List~PedidoPecaResponseDTO~~ listar()
    +ResponseEntity~PedidoPecaResponseDTO~ buscarPorId(Long id)
    -List~ItemPedidoPeca~ resolverItens(List~ItemPedidoPecaRequestDTO~ itens)
    -boolean possuiQuantidadeInvalida(List~ItemPedidoPecaRequestDTO~ itens)
    -boolean quantidadeInvalida(ItemPedidoPecaRequestDTO item)
}

class OrdemServicoController {
    -OrdemServicoRepository repository

    +OrdemServicoController()
    +ResponseEntity~OrdemServicoResponseDTO~ criar(OrdemServicoRequestDTO dto)
    +ResponseEntity~OrdemServicoResponseDTO~ atualizarStatus(Long id, AtualizarStatusOrdemDTO dto)
    +ResponseEntity~List~OrdemServicoResponseDTO~~ listar()
    +ResponseEntity~OrdemServicoResponseDTO~ buscarPorId(Long id)
}

%% =========================================================
%% RELACIONAMENTOS DOS MODELOS
%% =========================================================

Peca --> CategoriaPeca : categoria

Servico "1" o-- "0..*" Peca : pecasUtilizadas

ItemPedidoPeca --> Peca : peca

PedidoPeca "1" *-- "0..*" ItemPedidoPeca : itens

PedidoPeca --> StatusPedidoPeca : status

OrdemServico --> StatusOrdemServico : status

OrdemServico +-- Builder : possui

Builder ..> OrdemServico : cria

%% =========================================================
%% RELACIONAMENTOS DOS DTOs
%% =========================================================

PecaRequestDTO --> CategoriaPeca : categoria
PecaResponseDTO --> CategoriaPeca : categoria

PedidoPecaRequestDTO --> ItemPedidoPecaRequestDTO : itens

AdicionarItensPedidoRequestDTO --> ItemPedidoPecaRequestDTO : itens

AtualizarStatusPedidoRequestDTO --> StatusPedidoPeca : status

PedidoPecaResponseDTO --> StatusPedidoPeca : status
PedidoPecaResponseDTO --> ItemPedidoPecaResponseDTO : itens

AtualizarStatusOrdemDTO --> StatusOrdemServico : status
OrdemServicoResponseDTO --> StatusOrdemServico : status

%% =========================================================
%% RELACIONAMENTOS DOS MAPPERS
%% =========================================================

PecaMapper ..> Peca : converte
PecaMapper ..> PecaRequestDTO : recebe
PecaMapper ..> PecaResponseDTO : produz

ServicoMapper ..> Servico : converte
ServicoMapper ..> ServicoRequestDTO : recebe
ServicoMapper ..> ServicoResponseDTO : produz

PedidoPecaMapper ..> PedidoPeca : converte
PedidoPecaMapper ..> ItemPedidoPeca : converte
PedidoPecaMapper ..> PedidoPecaRequestDTO : recebe
PedidoPecaMapper ..> ItemPedidoPecaResponseDTO : produz
PedidoPecaMapper ..> PedidoPecaResponseDTO : produz

OrdemServicoMapper ..> OrdemServico : converte
OrdemServicoMapper ..> OrdemServicoRequestDTO : recebe
OrdemServicoMapper ..> OrdemServicoResponseDTO : produz

%% =========================================================
%% RELACIONAMENTOS DOS REPOSITÓRIOS
%% =========================================================

PecaRepository "1" o-- "0..*" Peca : pecas

ServicoRepository "1" o-- "0..*" Servico : servicos

PedidoPecaRepository "1" o-- "0..*" PedidoPeca : pedidos
PedidoPecaRepository ..> ItemPedidoPeca : manipula
PedidoPecaRepository ..> StatusPedidoPeca : atualiza

OrdemServicoRepository "1" o-- "0..*" OrdemServico : ordens
OrdemServicoRepository ..> StatusOrdemServico : atualiza

%% =========================================================
%% RELACIONAMENTOS DOS CONTROLLERS
%% =========================================================

PecaController --> PecaRepository : utiliza
PecaController ..> PecaRequestDTO : recebe
PecaController ..> PecaResponseDTO : retorna
PecaController ..> PecaMapper : utiliza

ServicoController --> ServicoRepository : utiliza
ServicoController ..> ServicoRequestDTO : recebe
ServicoController ..> ServicoResponseDTO : retorna
ServicoController ..> ServicoMapper : utiliza

PedidoPecaController --> PedidoPecaRepository : utiliza
PedidoPecaController --> PecaRepository : utiliza
PedidoPecaController ..> PedidoPecaRequestDTO : recebe
PedidoPecaController ..> AdicionarItensPedidoRequestDTO : recebe
PedidoPecaController ..> AtualizarStatusPedidoRequestDTO : recebe
PedidoPecaController ..> PedidoPecaResponseDTO : retorna
PedidoPecaController ..> ItemPedidoPecaRequestDTO : utiliza
PedidoPecaController ..> ItemPedidoPeca : cria
PedidoPecaController ..> PedidoPecaMapper : utiliza

OrdemServicoController --> OrdemServicoRepository : utiliza
OrdemServicoController ..> OrdemServicoRequestDTO : recebe
OrdemServicoController ..> AtualizarStatusOrdemDTO : recebe
OrdemServicoController ..> OrdemServicoResponseDTO : retorna
OrdemServicoController ..> OrdemServicoMapper : utiliza
```
---

# Diagrama de Classes

## Versão atualizada em 20/09/2026 conforme o código presente no projeto.

```mermaid
classDiagram

direction LR

%% =========================================================
%% ENUMERAÇÕES
%% =========================================================

class CategoriaPeca {
    <<enumeration>>
    MOTOR
    SUSPENSAO
    FREIOS
    ELETRICA
    ACESSORIOS
}

class StatusOrdemServico {
    <<enumeration>>
    ABERTO
    PENDENTE_PAGAMENTO
    PAGO
    EM_EXECUCAO
    EXECUTADO
}

class StatusPedidoPeca {
    <<enumeration>>
    ORCANDO
    PENDENTE_PAGAMENTO
    PAGO_FATURADO
    ENTREGUE
}

%% =========================================================
%% MODELOS
%% =========================================================

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

class ItemPedidoPeca {
    -Long id
    -Peca peca
    -Integer quantidade
    -Double precoUnitario

    +ItemPedidoPeca()
    +ItemPedidoPeca(Peca peca, Integer quantidade)
    +Long getId()
    +void setId(Long id)
    +Peca getPeca()
    +void setPeca(Peca peca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
    +Double getPrecoUnitario()
    +void setPrecoUnitario(Double precoUnitario)
    +Double getSubtotal()
}

class PedidoPeca {
    -Long id
    -StatusPedidoPeca status
    -LocalDateTime dataCriacao
    -LocalDateTime dataUltimaAtualizacao
    -List~ItemPedidoPeca~ itens

    +PedidoPeca()
    +Long getId()
    +void setId(Long id)
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
    +LocalDateTime getDataCriacao()
    +void setDataCriacao(LocalDateTime dataCriacao)
    +LocalDateTime getDataUltimaAtualizacao()
    +void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao)
    +List~ItemPedidoPeca~ getItens()
    +void setItens(List~ItemPedidoPeca~ itens)
    +void adicionarItem(ItemPedidoPeca item)
    +Double getValorTotal()
}

class OrdemServico {
    -Long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    -OrdemServico(Builder builder)
    +builder()$ : Builder
    +Long getId()
    +void setId(Long id)
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +LocalDate getDataAbertura()
    +void setDataAbertura(LocalDate dataAbertura)
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
    +double getValor()
    +void setValor(double valor)
}

class Builder {
    <<static nested class>>
    -long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -double valor

    +id(long id) : Builder
    +descricaoProblema(String descricaoProblema) : Builder
    +dataAbertura(LocalDate dataAbertura) : Builder
    +status(StatusOrdemServico status) : Builder
    +valor(double valor) : Builder
    +build() : OrdemServico
}

%% =========================================================
%% DTOs - PEÇA
%% =========================================================

class PecaRequestDTO {
    -String codigoBarras
    -String fornecedorMarca
    -Integer quantidadeEstoque
    -Double precoCusto
    -Double precoVenda
    -String tamanho
    -String cor
    -CategoriaPeca categoria

    +PecaRequestDTO()
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
    +String getTamanho()
    +void setTamanho(String tamanho)
    +String getCor()
    +void setCor(String cor)
    +CategoriaPeca getCategoria()
    +void setCategoria(CategoriaPeca categoria)
}

class PecaResponseDTO {
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

    +PecaResponseDTO()
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

%% =========================================================
%% DTOs - SERVIÇO
%% =========================================================

class ServicoRequestDTO {
    -String descricao
    -Double valorMaoDeObra
    -Double custoTabelado
    -Double tempoEstimadoMinutos

    +ServicoRequestDTO()
    +String getDescricao()
    +void setDescricao(String descricao)
    +Double getValorMaoDeObra()
    +void setValorMaoDeObra(Double valorMaoDeObra)
    +Double getCustoTabelado()
    +void setCustoTabelado(Double custoTabelado)
    +Double getTempoEstimadoMinutos()
    +void setTempoEstimadoMinutos(Double tempoEstimadoMinutos)
}

class ServicoResponseDTO {
    -Long id
    -String descricao
    -Double valorMaoDeObra
    -Double custoTabelado
    -Double tempoEstimadoMinutos
    -LocalDateTime dataCadastro
    -LocalDateTime dataAtualizacao

    +ServicoResponseDTO()
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
    +LocalDateTime getDataCadastro()
    +void setDataCadastro(LocalDateTime dataCadastro)
    +LocalDateTime getDataAtualizacao()
    +void setDataAtualizacao(LocalDateTime dataAtualizacao)
}

%% =========================================================
%% DTOs - PEDIDO DE PEÇAS
%% =========================================================

class ItemPedidoPecaRequestDTO {
    -Long codigoPeca
    -Integer quantidade

    +ItemPedidoPecaRequestDTO()
    +Long getCodigoPeca()
    +void setCodigoPeca(Long codigoPeca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
}

class ItemPedidoPecaResponseDTO {
    -Long id
    -Long codigoPeca
    -String codigoBarrasPeca
    -String fornecedorMarcaPeca
    -Integer quantidade
    -Double precoUnitario
    -Double subtotal

    +ItemPedidoPecaResponseDTO()
    +Long getId()
    +void setId(Long id)
    +Long getCodigoPeca()
    +void setCodigoPeca(Long codigoPeca)
    +String getCodigoBarrasPeca()
    +void setCodigoBarrasPeca(String codigoBarrasPeca)
    +String getFornecedorMarcaPeca()
    +void setFornecedorMarcaPeca(String fornecedorMarcaPeca)
    +Integer getQuantidade()
    +void setQuantidade(Integer quantidade)
    +Double getPrecoUnitario()
    +void setPrecoUnitario(Double precoUnitario)
    +Double getSubtotal()
    +void setSubtotal(Double subtotal)
}

class PedidoPecaRequestDTO {
    -List~ItemPedidoPecaRequestDTO~ itens

    +PedidoPecaRequestDTO()
    +List~ItemPedidoPecaRequestDTO~ getItens()
    +void setItens(List~ItemPedidoPecaRequestDTO~ itens)
}

class AdicionarItensPedidoRequestDTO {
    -List~ItemPedidoPecaRequestDTO~ itens

    +AdicionarItensPedidoRequestDTO()
    +List~ItemPedidoPecaRequestDTO~ getItens()
    +void setItens(List~ItemPedidoPecaRequestDTO~ itens)
}

class AtualizarStatusPedidoRequestDTO {
    -StatusPedidoPeca status

    +AtualizarStatusPedidoRequestDTO()
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
}

class PedidoPecaResponseDTO {
    -Long id
    -StatusPedidoPeca status
    -LocalDateTime dataCriacao
    -LocalDateTime dataUltimaAtualizacao
    -List~ItemPedidoPecaResponseDTO~ itens
    -Double valorTotal

    +PedidoPecaResponseDTO()
    +Long getId()
    +void setId(Long id)
    +StatusPedidoPeca getStatus()
    +void setStatus(StatusPedidoPeca status)
    +LocalDateTime getDataCriacao()
    +void setDataCriacao(LocalDateTime dataCriacao)
    +LocalDateTime getDataUltimaAtualizacao()
    +void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao)
    +List~ItemPedidoPecaResponseDTO~ getItens()
    +void setItens(List~ItemPedidoPecaResponseDTO~ itens)
    +Double getValorTotal()
    +void setValorTotal(Double valorTotal)
}

%% =========================================================
%% DTOs - ORDEM DE SERVIÇO
%% =========================================================

class OrdemServicoRequestDTO {
    -String descricaoProblema
    -Double valor

    +OrdemServicoRequestDTO()
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +Double getValor()
    +void setValor(Double valor)
}

class OrdemServicoResponseDTO {
    -Long id
    -String descricaoProblema
    -LocalDate dataAbertura
    -StatusOrdemServico status
    -Double valor

    +OrdemServicoResponseDTO()
    +Long getId()
    +void setId(Long id)
    +String getDescricaoProblema()
    +void setDescricaoProblema(String descricaoProblema)
    +LocalDate getDataAbertura()
    +void setDataAbertura(LocalDate dataAbertura)
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
    +Double getValor()
    +void setValor(Double valor)
}

class AtualizarStatusOrdemDTO {
    -StatusOrdemServico status

    +AtualizarStatusOrdemDTO()
    +StatusOrdemServico getStatus()
    +void setStatus(StatusOrdemServico status)
}

%% =========================================================
%% MAPPERS
%% =========================================================

class PecaMapper {
    +Peca paraModel(PecaRequestDTO dto)$
    +PecaResponseDTO paraResponseDTO(Peca peca)$
}

class ServicoMapper {
    <<final>>
    -ServicoMapper()
    +Servico paraModel(ServicoRequestDTO dto)$
    +ServicoResponseDTO paraResponseDTO(Servico servico)$
}

class PedidoPecaMapper {
    +PedidoPeca paraModel(PedidoPecaRequestDTO dto)$
    +ItemPedidoPecaResponseDTO paraItemResponseDTO(ItemPedidoPeca item)$
    +PedidoPecaResponseDTO paraResponseDTO(PedidoPeca pedido)$
}

class OrdemServicoMapper {
    <<final>>
    -OrdemServicoMapper()
    +OrdemServico paraModel(OrdemServicoRequestDTO dto)$
    +OrdemServicoResponseDTO paraResponseDTO(OrdemServico ordem)$
}

%% =========================================================
%% REPOSITÓRIOS
%% =========================================================

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

class PedidoPecaRepository {
    -PedidoPecaRepository instance$
    -List~PedidoPeca~ pedidos
    -Long proximoIdPedido
    -Long proximoIdItem

    -PedidoPecaRepository()
    +PedidoPecaRepository getInstance()$
    +PedidoPeca salvar(PedidoPeca pedido)
    +PedidoPeca adicionarItens(Long idPedido, List~ItemPedidoPeca~ itens)
    +Optional~PedidoPeca~ atualizarStatus(Long idPedido, StatusPedidoPeca status)
    +List~PedidoPeca~ listar()
    +Optional~PedidoPeca~ buscarPorId(Long id)
}

class OrdemServicoRepository {
    -OrdemServicoRepository instance$
    -List~OrdemServico~ ordens
    -long proximoId

    -OrdemServicoRepository()
    +OrdemServicoRepository getInstance()$
    +OrdemServico salvar(OrdemServico ordem)
    +List~OrdemServico~ listar()
    +Optional~OrdemServico~ buscarPorId(Long id)
    +Optional~OrdemServico~ atualizarStatus(Long id, StatusOrdemServico status)
}

%% =========================================================
%% CONTROLLERS
%% =========================================================

class PecaController {
    -PecaRepository repository

    +PecaController()
    +ResponseEntity~PecaResponseDTO~ cadastrar(PecaRequestDTO dto)
    +ResponseEntity~List~PecaResponseDTO~~ listar()
    +ResponseEntity~PecaResponseDTO~ buscarPorCodigo(Long codigo)
    +ResponseEntity~PecaResponseDTO~ atualizar(Long codigo, PecaRequestDTO dto)
    +ResponseEntity~Void~ excluir(Long codigo)
}

class ServicoController {
    -ServicoRepository repository

    +ServicoController()
    +ResponseEntity~ServicoResponseDTO~ cadastrar(ServicoRequestDTO dto)
    +ResponseEntity~List~ServicoResponseDTO~~ listar()
    +ResponseEntity~ServicoResponseDTO~ buscarPorId(Long id)
    +ResponseEntity~ServicoResponseDTO~ atualizar(Long id, ServicoRequestDTO dto)
    +ResponseEntity~Void~ excluir(Long id)
}

class PedidoPecaController {
    -PedidoPecaRepository pedidoRepository
    -PecaRepository pecaRepository

    +PedidoPecaController()
    +ResponseEntity~PedidoPecaResponseDTO~ criarPedido(PedidoPecaRequestDTO dto)
    +ResponseEntity~PedidoPecaResponseDTO~ adicionarItens(Long idPedido, AdicionarItensPedidoRequestDTO dto)
    +ResponseEntity~PedidoPecaResponseDTO~ atualizarStatus(Long idPedido, AtualizarStatusPedidoRequestDTO dto)
    +ResponseEntity~List~PedidoPecaResponseDTO~~ listar()
    +ResponseEntity~PedidoPecaResponseDTO~ buscarPorId(Long id)
    -List~ItemPedidoPeca~ resolverItens(List~ItemPedidoPecaRequestDTO~ itens)
    -boolean possuiQuantidadeInvalida(List~ItemPedidoPecaRequestDTO~ itens)
    -boolean quantidadeInvalida(ItemPedidoPecaRequestDTO item)
}

class OrdemServicoController {
    -OrdemServicoRepository repository

    +OrdemServicoController()
    +ResponseEntity~OrdemServicoResponseDTO~ criar(OrdemServicoRequestDTO dto)
    +ResponseEntity~OrdemServicoResponseDTO~ atualizarStatus(Long id, AtualizarStatusOrdemDTO dto)
    +ResponseEntity~List~OrdemServicoResponseDTO~~ listar()
    +ResponseEntity~OrdemServicoResponseDTO~ buscarPorId(Long id)
}

%% =========================================================
%% RELACIONAMENTOS DOS MODELOS
%% =========================================================

Peca --> CategoriaPeca : categoria

Servico "1" o-- "0..*" Peca : pecasUtilizadas

ItemPedidoPeca --> Peca : peca

PedidoPeca "1" *-- "0..*" ItemPedidoPeca : itens

PedidoPeca --> StatusPedidoPeca : status

OrdemServico --> StatusOrdemServico : status

OrdemServico +-- Builder : possui

Builder ..> OrdemServico : cria

%% =========================================================
%% RELACIONAMENTOS DOS DTOs
%% =========================================================

PecaRequestDTO --> CategoriaPeca : categoria
PecaResponseDTO --> CategoriaPeca : categoria

PedidoPecaRequestDTO --> ItemPedidoPecaRequestDTO : itens

AdicionarItensPedidoRequestDTO --> ItemPedidoPecaRequestDTO : itens

AtualizarStatusPedidoRequestDTO --> StatusPedidoPeca : status

PedidoPecaResponseDTO --> StatusPedidoPeca : status
PedidoPecaResponseDTO --> ItemPedidoPecaResponseDTO : itens

AtualizarStatusOrdemDTO --> StatusOrdemServico : status
OrdemServicoResponseDTO --> StatusOrdemServico : status

%% =========================================================
%% RELACIONAMENTOS DOS MAPPERS
%% =========================================================

PecaMapper ..> Peca : converte
PecaMapper ..> PecaRequestDTO : recebe
PecaMapper ..> PecaResponseDTO : produz

ServicoMapper ..> Servico : converte
ServicoMapper ..> ServicoRequestDTO : recebe
ServicoMapper ..> ServicoResponseDTO : produz

PedidoPecaMapper ..> PedidoPeca : converte
PedidoPecaMapper ..> ItemPedidoPeca : converte
PedidoPecaMapper ..> PedidoPecaRequestDTO : recebe
PedidoPecaMapper ..> ItemPedidoPecaResponseDTO : produz
PedidoPecaMapper ..> PedidoPecaResponseDTO : produz

OrdemServicoMapper ..> OrdemServico : converte
OrdemServicoMapper ..> OrdemServicoRequestDTO : recebe
OrdemServicoMapper ..> OrdemServicoResponseDTO : produz

%% =========================================================
%% RELACIONAMENTOS DOS REPOSITÓRIOS
%% =========================================================

PecaRepository "1" o-- "0..*" Peca : pecas

ServicoRepository "1" o-- "0..*" Servico : servicos

PedidoPecaRepository "1" o-- "0..*" PedidoPeca : pedidos
PedidoPecaRepository ..> ItemPedidoPeca : manipula
PedidoPecaRepository ..> StatusPedidoPeca : atualiza

OrdemServicoRepository "1" o-- "0..*" OrdemServico : ordens
OrdemServicoRepository ..> StatusOrdemServico : atualiza

%% =========================================================
%% RELACIONAMENTOS DOS CONTROLLERS
%% =========================================================

PecaController --> PecaRepository : utiliza
PecaController ..> PecaRequestDTO : recebe
PecaController ..> PecaResponseDTO : retorna
PecaController ..> PecaMapper : utiliza

ServicoController --> ServicoRepository : utiliza
ServicoController ..> ServicoRequestDTO : recebe
ServicoController ..> ServicoResponseDTO : retorna
ServicoController ..> ServicoMapper : utiliza

PedidoPecaController --> PedidoPecaRepository : utiliza
PedidoPecaController --> PecaRepository : utiliza
PedidoPecaController ..> PedidoPecaRequestDTO : recebe
PedidoPecaController ..> AdicionarItensPedidoRequestDTO : recebe
PedidoPecaController ..> AtualizarStatusPedidoRequestDTO : recebe
PedidoPecaController ..> PedidoPecaResponseDTO : retorna
PedidoPecaController ..> ItemPedidoPecaRequestDTO : utiliza
PedidoPecaController ..> ItemPedidoPeca : cria
PedidoPecaController ..> PedidoPecaMapper : utiliza

OrdemServicoController --> OrdemServicoRepository : utiliza
OrdemServicoController ..> OrdemServicoRequestDTO : recebe
OrdemServicoController ..> AtualizarStatusOrdemDTO : recebe
OrdemServicoController ..> OrdemServicoResponseDTO : retorna
OrdemServicoController ..> OrdemServicoMapper : utiliza
```