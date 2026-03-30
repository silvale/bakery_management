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


CREATE TABLE formula (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    product_code VARCHAR(50) NOT NULL, -- FINISHED
    version INTEGER NOT NULL,
    description VARCHAR(255),
    loss_rate NUMERIC(5,2), -- % hao hụt
	component_type VARCHAR(20), -- MAIN / SUB / PACKAGING
    is_active BOOLEAN DEFAULT TRUE,
    status varchar(255),
	created_at timestamptz(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) DEFAULT 'SYSTEM' NOT NULL,
	updated_at timestamptz,
	updated_by varchar(255) null,

    CONSTRAINT fk_formula_product FOREIGN KEY (product_code)
        REFERENCES product(code),

    CONSTRAINT uk_formula_version UNIQUE (product_code, version)
);

CREATE TABLE formula_component (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    formula_id UUID NOT NULL,
    product_code VARCHAR(50) NOT NULL, -- RAW / SEMI
    quantity NUMERIC(18,6) NOT NULL,
    unit_code VARCHAR(50) NOT NULL, -- gram, kg...
    note VARCHAR(255),
   status varchar(255),
	created_at timestamptz(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) DEFAULT 'SYSTEM' NOT NULL,
	updated_at timestamptz,
	updated_by varchar(255) null,
    CONSTRAINT fk_formula_component_formula FOREIGN KEY (formula_id)
        REFERENCES formula(id),

    CONSTRAINT fk_formula_component_product FOREIGN KEY (product_code)
        REFERENCES product(code)
);