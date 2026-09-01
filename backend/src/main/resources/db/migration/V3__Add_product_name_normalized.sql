ALTER TABLE product ADD COLUMN name_normalized VARCHAR(255);

UPDATE product SET name_normalized = LOWER(name);

CREATE INDEX idx_product_name_normalized ON product(name_normalized);
