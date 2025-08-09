CREATE SEQUENCE IF NOT EXISTS contact_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE contact
(
    id           BIGINT      NOT NULL,
    surname      VARCHAR(50) NOT NULL,
    name         VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    CONSTRAINT pk_contact PRIMARY KEY (id)
);

ALTER TABLE contact
    ADD CONSTRAINT uc_contact_phonenumber UNIQUE (phone_number);