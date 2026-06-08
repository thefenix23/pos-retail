-- ===================================================
-- V2__seed_catalogs.sql
-- Seed data: payment methods and categories
-- Oracle 19c compatible
-- ===================================================

INSERT INTO payment_methods (name, active) VALUES ('Cash', 1);
INSERT INTO payment_methods (name, active) VALUES ('Credit Card', 1);
INSERT INTO payment_methods (name, active) VALUES ('Debit Card', 1);
INSERT INTO payment_methods (name, active) VALUES ('Transfer', 1);

INSERT INTO categories (name, description, active) VALUES ('General', 'Default category', 1);
INSERT INTO categories (name, description, active) VALUES ('Food', 'Food and beverages', 1);
INSERT INTO categories (name, description, active) VALUES ('Electronics', 'Electronic devices', 1);
INSERT INTO categories (name, description, active) VALUES ('Clothing', 'Apparel and footwear', 1);