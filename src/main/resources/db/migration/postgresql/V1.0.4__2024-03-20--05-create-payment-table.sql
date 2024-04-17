CREATE TABLE payment
(
    transaction_id   VARCHAR(36)    NOT NULL DEFAULT gen_random_uuid(),
    language         VARCHAR(20)    NOT NULL,
    notification_url VARCHAR(255)   NOT NULL,
    message          VARCHAR(255)   NOT NULL,
    type             VARCHAR(32)     NOT NULL, -- top up, payout, etc
    payment_method   VARCHAR(8)     NOT NULL, -- card, cash, etc
    currency         VARCHAR(8)     NOT NULL, -- RUB, USD, EUR, etc
    amount           NUMERIC(16, 2) NOT NULL,

    card_id          VARCHAR(36)    NOT NULL,
    account_id       VARCHAR(36)    NOT NULL,

    created_at       TIMESTAMP      NOT NULL,
    created_by       VARCHAR(64)    NOT NULL,
    updated_at       TIMESTAMP      NOT NULL,
    updated_by       VARCHAR(64)    NOT NULL,
    status           VARCHAR(16)    NOT NULL,

    PRIMARY KEY (transaction_id),
    CONSTRAINT fk_payment_card FOREIGN KEY (card_id) REFERENCES card (id),
    CONSTRAINT fk_payment_account FOREIGN KEY (account_id) REFERENCES account (id)
);
