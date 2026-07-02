-- ===================================================
-- V3__sale_module.sql
-- Módulo de venta: ventas, sus líneas y sus pagos.
-- Forma final de Fase 2: la venta nace ya vinculada a una sesión de caja
-- y admite varios pagos (no hay payment_method_id en sales).
-- Depende de cash_sessions, products y payment_methods (módulos previos).
-- Compatible con Oracle 19c
-- ==================================================

-- Venta: pertenece a una sesión de caja abierta.
CREATE TABLE sales (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    status VARCHAR2(20) DEFAULT 'COMPLETED' NOT NULL ,
    total NUMBER(12,2) NOT NULL CHECK ( total >= 0 ) ,
    cash_session_id NUMBER(19) NOT NULL REFERENCES cash_sessions(id) ,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

-- Líneas de la venta: producto, cantidad, precio y subtotal.
CREATE TABLE sale_items (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    sale_id NUMBER(19) NOT NULL REFERENCES sales(id) ON DELETE CASCADE ,
    product_id NUMBER(19) NOT NULL REFERENCES products(id) ,
    quantity NUMBER(10) NOT NULL CHECK ( quantity >= 0 ) ,
    unit_price NUMBER(12,2) NOT NULL CHECK ( unit_price >= 0 ) ,
    subtotal NUMBER(12,2) NOT NULL CHECK ( subtotal >= 0 )
);

-- Pagos de la venta: una venta puede pagarse con varios medios (pagos mixtos).
CREATE TABLE sale_payments (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    sale_id NUMBER(19) NOT NULL REFERENCES sales(id) ON DELETE CASCADE ,
    payment_method_id NUMBER(19) NOT NULL REFERENCES payment_methods(id),
    amount NUMBER(12,2) NOT NULL CHECK ( amount > 0 )
);

CREATE INDEX idx_sale_items_sale ON sale_items(sale_id);
CREATE INDEX idx_sale_payments_sale ON sale_payments(sale_id);
CREATE INDEX idx_sale_cash_session ON sales(cash_session_id);