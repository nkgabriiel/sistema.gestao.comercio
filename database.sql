CREATE DATABASE IF NOT EXISTS  sistema_gestao_pesqueiro;
use sistema_gestao_pesqueiro;

CREATE TABLE IF NOT EXISTS cliente
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR (255) UNIQUE NOT NULL,
    ponto_fidelidade INT
    );

CREATE TABLE IF NOT EXISTS funcionario (
                                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                           nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    matricula VARCHAR(255) UNIQUE NOT NULL,
    cargo VARCHAR(255) NOT NULL,
    percentual_comissao DOUBLE NOT NULL
    );

CREATE TABLE IF NOT EXISTS produto (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    precoCusto DOUBLE NOT NULL,
    precoVenda DOUBLE NOT NULL,
    quantidade_atual INT NOT NULL,
    quantidade_minima INT NOT NULL
    );

ALTER TABLE produto RENAME COLUMN precoCusto TO preco_custo;
ALTER TABLE produto RENAME COLUMN precoVenda TO preco_venda;

CREATE TABLE IF NOT EXISTS fornecedor (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          nome_representante VARCHAR(255) NOT NULL,
    contato_representante VARCHAR(20) NOT NULL,
    nome_fantasia VARCHAR(255) NOT NULL,
    cnpj VARCHAR (18) UNIQUE NOT NULL
    );

ALTER TABLE produto
    ADD COLUMN fornecedor_id BIGINT NOT NULL,
ADD CONSTRAINT fk_produto_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id);

CREATE TABLE IF NOT EXISTS venda (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     funcionario_id BIGINT NOT NULL,
                                     cliente_id BIGINT NULL,
                                     data DATETIME NOT NULL,
                                     forma_pagamento VARCHAR(50) NOT NULL,
    valor_total DOUBLE NOT NULL,
    valor_cobrado DOUBLE NOT NUll,

    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
    );

CREATE TABLE IF NOT EXISTS item_venda (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          venda_id BIGINT NOT NULL,
                                          produto_id BIGINT NOT NULL,
                                          quantidade INT NOT NULL,
                                          preco_unitario DOUBLE NOT NULL,

                                          FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
    );
