-- ===================================================
-- V1__create_core_tables.sql
-- Core tables for Pos Retail - Phase 1: Sale Nucleus
-- Oracle 19c compatible
-- ===================================================

CREATE TABLE categories (
    id          NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL UNIQUE,
    description VARCHAR2(255),
    active      NUMBER(1)     DEFAULT 1 NOT NULL CHECK (active IN (0,1)),
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE TABLE products (
    id          NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sku         VARCHAR2(50)  NOT NULL UNIQUE,
    name        VARCHAR2(150) NOT NULL,
    description VARCHAR2(255),
    price       NUMBER(12,2)  NOT NULL CHECK (price >= 0),
    stock       NUMBER(10)    DEFAULT 0 NOT NULL CHECK (stock >= 0),
    active      NUMBER(1)     DEFAULT 1 NOT NULL CHECK (active IN (0,1)),
    category_id NUMBER(19)    REFERENCES categories(id) ON DELETE SET NULL,
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE TABLE payment_methods (
    id     NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name   VARCHAR2(50) NOT NULL UNIQUE,
    active NUMBER(1)    DEFAULT 1 NOT NULL CHECK (active IN (0,1))
);

CREATE TABLE sales (
    id                NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status            VARCHAR2(20) DEFAULT 'COMPLETED' NOT NULL,
    total             NUMBER(12,2) NOT NULL CHECK (total >= 0),
    payment_method_id NUMBER(19)   NOT NULL REFERENCES payment_methods(id),
    created_at        TIMESTAMP    DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE TABLE sale_items (
    id         NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sale_id    NUMBER(19) NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    product_id NUMBER(19) NOT NULL REFERENCES products(id),
    quantity   NUMBER(10) NOT NULL CHECK (quantity >= 0),
    unit_price NUMBER(12,2) NOT NULL CHECK (unit_price >= 0),
    subtotal   NUMBER(12,2) NOT NULL CHECK (subtotal >= 0)
);

CREATE INDEX idx_products_name   ON products(name);
CREATE INDEX idx_sale_items_sale ON sale_items(sale_id);
