ALTER TABLE inventory ADD column if not exists process_date date;

ALTER TABLE formula ADD column if not exists formula_id UUID;
ALTER TABLE formula ADD column if not exists formula_version INTEGER;

-- FK product
ALTER TABLE formula
ADD CONSTRAINT fk_formula_product
FOREIGN KEY (product_code)
REFERENCES product(code);

-- chỉ 1 formula active / product
CREATE UNIQUE INDEX uk_formula_active
ON formula(product_code)
WHERE is_active = true;