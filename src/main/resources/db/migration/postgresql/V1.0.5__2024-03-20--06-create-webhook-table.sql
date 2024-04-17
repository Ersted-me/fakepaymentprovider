CREATE TABLE webhook
(
    id            VARCHAR(36)  NOT NULL DEFAULT gen_random_uuid(),
    url           VARCHAR(255) NOT NULL,
    request       VARCHAR(4000) NOT NULL,
    response      VARCHAR(4000) NOT NULL,
    code          VARCHAR(3)   NOT NULL,
    number_retry  INT          NOT NULL,
    last_retry_at TIMESTAMP    NOT NULL,

    payment_id    VARCHAR(36)  NOT NULL,

    created_at    TIMESTAMP    NOT NULL,
    created_by    VARCHAR(64)  NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    updated_by    VARCHAR(64)  NOT NULL,
    status        VARCHAR(16)  NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_webhook_payment FOREIGN KEY (payment_id) REFERENCES payment (transaction_id)
);
