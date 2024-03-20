CREATE TABLE account
(
    id         SERIAL         NOT NULL,
    currency   VARCHAR(3)     NOT NULL,
    balance    NUMERIC(16, 2) NOT NULL,
    merchant_id VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    created_by VARCHAR(64)    NOT NULL,
    updated_at TIMESTAMP      NOT NULL,
    updated_by VARCHAR(64)    NOT NULL,
    status     VARCHAR(16)    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(merchant_id)
);