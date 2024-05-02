CREATE TABLE IF NOT EXISTS user_manager (
    id SERIAL PRIMARY KEY,
    loja_name VARCHAR(255),
    user_id_ml VARCHAR(255),
    access_code VARCHAR(255),
    refresh_token VARCHAR(255),
    created_at TIMESTAMP,
    cep VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS anuncio(
    id SERIAL PRIMARY KEY,
        ml_id VARCHAR(30) NOT NULL,
        sku VARCHAR(255) NOT NULL,
        gtin VARCHAR(255),
        url VARCHAR(255) NOT NULL,
        descricao VARCHAR(255) NOT NULL,
        categoria VARCHAR(255) NOT NULL,
        custo NUMERIC(18,2) NOT NULL,
        csosn VARCHAR(5),
        preco_desconto NUMERIC(18,2) NOT NULL,
        taxa_ml NUMERIC(18,2) NOT NULL,
        custo_frete NUMERIC(18,2) NOT NULL,
        status VARCHAR(255) NOT NULL,
        listing_type SMALLINT NOT NULL,
        created_at TIMESTAMP NOT NULL,
        user_id_fk BIGINT NOT NULL,
        complete BOOLEAN NOT NULL,
        is_fulfillment BOOLEAN NOT NULL,
        FOREIGN KEY (user_id_fk) REFERENCES user_manager(id)
);

CREATE TABLE IF NOT EXISTS orderM (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    shipping_id BIGINT NOT NULL,
    order_creation_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reclamacao (
    id SERIAL PRIMARY KEY,
    reclamacao_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    stage VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason VARCHAR(50) NOT NULL,
    closed_by VARCHAR(200) NOT NULL,
    benefited VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    reclamacao_created_at TIMESTAMP NOT NULL,
    order_id_fk BIGINT NOT NULL,
    FOREIGN KEY (order_id_fk) REFERENCES orderM(id)
);

CREATE TABLE IF NOT EXISTS schedule_job (
    id SERIAL PRIMARY KEY,
    job_name VARCHAR(300) NOT NULL,
    job_group_name VARCHAR(300) NOT NULL,
    next_run_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS url (
    id SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    anuncio_id_fk BIGINT NOT NULL,
    FOREIGN KEY (anuncio_id_fk) REFERENCES anuncio(id)
);

CREATE TABLE IF NOT EXISTS venda (
    id SERIAL PRIMARY KEY,
    quantidade INTEGER NOT NULL,
    preco_desconto NUMERIC(18,2) NOT NULL,
    taxa_ml NUMERIC(18,2) NOT NULL,
    custo_frete NUMERIC(18,2) NOT NULL,
    custo NUMERIC(18,2) NOT NULL,
    lucro NUMERIC(18,2) NOT NULL,
    completo BOOLEAN NOT NULL,
    status VARCHAR(255) NOT NULL,
    anuncio_id_fk BIGINT NOT NULL,
    order_id_fk BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (anuncio_id_fk) REFERENCES anuncio(id),
    FOREIGN KEY (order_id_fk) REFERENCES orderM(id)
);

