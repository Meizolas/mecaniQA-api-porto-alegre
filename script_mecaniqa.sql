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