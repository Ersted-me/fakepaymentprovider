CREATE TABLE merchant
(
    merchant_id VARCHAR(50)  NOT NULL,
    secret_key  VARCHAR(255) NOT NULL,

    created_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(64)  NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    updated_by  VARCHAR(64)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,

    PRIMARY KEY (merchant_id)
);