CREATE TABLE card
(
    id          SERIAL         NOT NULL,
    card_number VARCHAR(19)    NOT NULL,
    exp_date    TIMESTAMP      NOT NULL,
    cvv         VARCHAR(3)     NOT NULL,
    balance     NUMERIC(16, 2) NOT NULL,
    customer_id SERIAL         NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    created_by  VARCHAR(64)    NOT NULL,
    updated_at  TIMESTAMP      NOT NULL,
    updated_by  VARCHAR(64)    NOT NULL,
    status      VARCHAR(16)    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_card_customer FOREIGN KEY (customer_id) references customer(id)
);