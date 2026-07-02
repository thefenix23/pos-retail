-- ===================================================
-- V4__seed_catalogs.sql
-- Datos semilla: métodos de pago y categorías iniciales.
-- Compatible con Oracle 19c
-- ===================================================

INSERT INTO payment_methods (name, active) VALUES ('Cash', 1);
INSERT INTO payment_methods (name, active) VALUES ('Credit Card', 1);
INSERT INTO payment_methods (name, active) VALUES ('Debit Card', 1);
INSERT INTO payment_methods (name, active) VALUES ('Transfer', 1);

INSERT INTO categories (name, description, active) VALUES ('General', 'Categoria predeterminada', 1);
INSERT INTO categories (name, description, active) VALUES ('Comida', 'Alimentos y bebidas', 1);
INSERT INTO categories (name, description, active) VALUES ('Electronica', 'Dispositivos electrónicos', 1);
INSERT INTO categories (name, description, active) VALUES ('Ropa', 'Prendas de vestir y calzado', 1);