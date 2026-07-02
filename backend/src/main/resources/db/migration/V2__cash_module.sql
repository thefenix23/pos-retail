-- ===================================================
-- V2__cash_module.sql
-- Módulo de caja: sesiones, movimientos de efectivo y cortes.
-- Compatible con Oracle 19c
-- ===================================================

-- Sesión de caja: un turno abierto por un cajero con un fondo inicial.
CREATE TABLE cash_sessions (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    branch_id NUMBER(19) NOT NULL ,
    register_id NUMBER(19) NOT NULL ,
    cashier VARCHAR2(100) NOT NULL ,
    initial_fund NUMBER(12,2) NOT NULL CHECK ( initial_fund >= 0 ) ,
    status VARCHAR2(10) DEFAULT 'OPEN' NOT NULL CHECK ( status IN ('OPEN', 'CLOSED')) ,
    opened_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL ,
    closed_at TIMESTAMP
);

-- Garantiza que solo exista UNA sesión OPEN por (branch_id, register_id).
-- Oracle indexa solo filas con valores no nulos en el índice de función:
-- cuando status == 'CLOSED' la expresión es NULL y no ocupa el índice único,
-- por lo que pueden coexistir muchas sesiones cerradas, pero solo una abierta.
CREATE UNIQUE INDEX uq_cash_session_open ON cash_sessions (
        CASE WHEN status = 'OPEN' THEN branch_id END ,
        CASE WHEN status = 'OPEN' THEN register_id END
    );

CREATE INDEX idx_cash_sessions_status ON cash_sessions(status);

-- Movimientos de efectivo ajenos a ventas: retiros, depositos, gastos menores.
-- El signo lo da el tipo (INCOME suma, EXPENSE resta); amount siempre positivo.
CREATE TABLE cash_movements (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    cash_session_id NUMBER(19) NOT NULL REFERENCES cash_sessions(id) ,
    type VARCHAR2(10) NOT NULL CHECK ( type IN ('INCOME', 'EXPENSE')) ,
    concept VARCHAR2(255) NOT NULL ,
    amount NUMBER(12,2) NOT NULL CHECK ( amount > 0 ) ,
    responsible VARCHAR2(100) NOT NULL ,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

-- Corte de caja: el arqueo que se genera al cerrar el turno.
-- Una sola fila por sesion (UNIQUE), por eso es un cierre definitivo.
CREATE TABLE cash_counts (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    cash_session_id NUMBER(19) NOT NULL UNIQUE REFERENCES cash_sessions(id) ,
    expected_cash NUMBER(12,2) NOT NULL ,
    counted_cash NUMBER(12,2) NOT NULL ,
    difference NUMBER(12,2) NOT NULL ,
    approved_by VARCHAR2(100) NOT NULL ,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE INDEX idx_cash_movements_session ON cash_movements(cash_session_id);