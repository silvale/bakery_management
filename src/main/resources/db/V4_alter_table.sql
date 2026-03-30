ALTER TABLE inventory ADD column if not exists process_date date;

ALTER TABLE product_prices ADD column if not exists formula_id UUID;
ALTER TABLE product_prices ADD column if not exists formula_version INTEGER;

ALTER TABLE formula RENAME COLUMN formula_version TO formula_versions;



CREATE TABLE if not exists unit_conversion (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    from_unit_code varchar(20) REFERENCES unit(code),
    to_unit_code varchar(20) REFERENCES unit(code),
    ratio  NUMERIC(18,6),
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

-- chỉ 1 formula active / product
CREATE UNIQUE INDEX uk_formula_active
ON formula(product_code)
WHERE is_active = true;