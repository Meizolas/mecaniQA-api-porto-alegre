CREATE DATABASE IF NOT EXISTS mecaniqa;
    
USE mecaniqa;

CREATE TABLE IF NOT EXISTS pecas (
    codigo BIGINT NOT NULL AUTO_INCREMENT,
    codigo_barras VARCHAR(255), 
    fornecedor_marca VARCHAR(255),
    quantidade_estoque INT,
    preco_custo DECIMAL(12,2),
    preco_venda DECIMAL(12,2),
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tamanho VARCHAR(255),
    cor VARCHAR(255),
    categoria VARCHAR(30),

    PRIMARY KEY (codigo)
);

-- US03/US04/US05 - Pedidos de peças (Marcelo)
CREATE TABLE IF NOT EXISTS pedidos_pecas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status VARCHAR(30) NOT NULL DEFAULT 'ORCANDO',
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);

-- Entidade associativa entre pedido e peça
CREATE TABLE IF NOT EXISTS itens_pedido_peca (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    codigo_peca BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(12,2),

    PRIMARY KEY (id),
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedidos_pecas (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_peca FOREIGN KEY (codigo_peca)
        REFERENCES pecas (codigo)
);
