CREATE TABLE account
(
    id          VARCHAR(36)    NOT NULL DEFAULT gen_random_uuid(),
    currency    VARCHAR(3)     NOT NULL,
    balance     NUMERIC(16, 2) NOT NULL,

    merchant_id VARCHAR(50)    NOT NULL,

    created_at  TIMESTAMP      NOT NULL,
    created_by  VARCHAR(64)    NOT NULL,
    updated_at  TIMESTAMP      NOT NULL,
    updated_by  VARCHAR(64)    NOT NULL,
    status      VARCHAR(16)    NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_account_merchant FOREIGN KEY (merchant_id) REFERENCES merchant (merchant_id)
);

INSERT INTO public.account (id, currency, balance, merchant_id,
                            created_at, created_by,
                            updated_at, updated_by,
                            status)
VALUES (DEFAULT, 'BRL', 0.00, 'PROSELYTE',
        '2024-03-25 19:25:23.000000', 'auto',
        '2024-03-25 19:25:23.000000', 'auto',
        'ACTIVE');

INSERT INTO public.account (id, currency, balance, merchant_id,
                            created_at, created_by,
                            updated_at, updated_by,
                            status)
VALUES (DEFAULT, 'RUB', 0.00, 'PROSELYTE',
        '2024-03-25 19:25:23.000000', 'auto',
        '2024-03-25 19:25:23.000000', 'auto',
        'ACTIVE');
