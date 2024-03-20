CREATE TABLE customer
(
    id         SERIAL      NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name  VARCHAR(64) NOT NULL,
    country    VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(64) NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    updated_by VARCHAR(64) NOT NULL,
    status     VARCHAR(16) NOT NULL,
    PRIMARY KEY (id)
);