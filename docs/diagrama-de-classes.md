# Diagrama de Classes

Diagrama atualizado para representar a implementação atual da API, com armazenamento em memória e repositories no padrão Singleton.

```mermaid
classDiagram
direction LR

class Peca {
  - codigo : Long
  - codigoBarras : String
  - fornecedorMarca : String
  - quantidadeEstoque : Integer
  - precoCusto : Double
  - precoVenda : Double
  - dataCadastro : LocalDateTime
  - dataUltimaAtualizacao : LocalDateTime
  - tamanho : String
  - cor : String
  - categoria : CategoriaPeca
}

class CategoriaPeca {
  <<enumeration>>
  MOTOR
  SUSPENSAO
  FREIOS
  ELETRICA
  ACESSORIOS
}

class Servico {
  - id : Long
  - descricao : String
  - valorMaoDeObra : Double
  - custoTabelado : Double
  - tempoEstimadoMinutos : Double
  - pecasUtilizadas : List~Peca~
  - dataCadastro : LocalDateTime
  - dataAtualizacao : LocalDateTime
  + adicionarPeca(Peca peca) void
}

class PecaRepository {
  - instance$ : PecaRepository
  - pecas : List~Peca~
  - proximoCodigo : Long
  - PecaRepository()
  + getInstance()$ PecaRepository
  + salvar(Peca peca) Peca
  + listar() List~Peca~
  + buscarPorCodigo(Long codigo) Optional~Peca~
  + atualizar(Long codigo, Peca novosDados) Optional~Peca~
  + excluir(Long codigo) boolean
}

class ServicoRepository {
  - instance$ : ServicoRepository
  - servicos : List~Servico~
  - proximoId : Long
  - ServicoRepository()
  + getInstance()$ ServicoRepository
  + salvar(Servico servico) Servico
  + listar() List~Servico~
  + buscarPorId(Long id) Optional~Servico~
  + atualizar(Long id, Servico novosDados) Optional~Servico~
  + excluir(Long id) boolean
}

class PecaController {
  - repository : PecaRepository
  + cadastrar(Peca peca) ResponseEntity~Peca~
  + listar() ResponseEntity~List~Peca~~
  + buscarPorCodigo(Long codigo) ResponseEntity~Peca~
  + atualizar(Long codigo, Peca novosDados) ResponseEntity~Peca~
  + excluir(Long codigo) ResponseEntity~Void~
}

class ServicoController {
  - repository : ServicoRepository
  + cadastrar(Servico servico) ResponseEntity~Servico~
  + listar() ResponseEntity~List~Servico~~
  + buscarPorId(Long id) ResponseEntity~Servico~
  + atualizar(Long id, Servico novosDados) ResponseEntity~Servico~
  + excluir(Long id) ResponseEntity~Void~
}

Peca --> CategoriaPeca : categoria
Servico "1" o-- "0..*" Peca : pecasUtilizadas
PecaRepository "1" o-- "0..*" Peca : pecas
ServicoRepository "1" o-- "0..*" Servico : servicos
PecaController --> PecaRepository
ServicoController --> ServicoRepository
```
