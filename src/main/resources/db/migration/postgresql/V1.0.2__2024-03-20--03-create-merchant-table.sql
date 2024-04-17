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

INSERT INTO public.merchant (merchant_id, secret_key,
                             created_at, created_by,
                             updated_at, updated_by,
                             status)
VALUES ('PROSELYTE', 'b2eeea3e27834b7499dd7e01143a23dd',
        '2024-03-25 19:00:00.000000', 'manual',
        '2024-03-25 19:00:00.000000', 'manual',
        'ACTIVE')