# Diagrama de Classes

Versão oficial, atualizada em 29/08/2026 conforme o código presente no projeto.

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
