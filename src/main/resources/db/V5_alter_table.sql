
ALTER TABLE product
ADD COLUMN if not EXISTS supplier_code VARCHAR(50);

CREATE table if not EXISTS product_inventory_config (
    product_code VARCHAR(50),
    warehouse_type VARCHAR(50),
    warning_quantity NUMERIC,
    created_at timestamptz(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) DEFAULT 'SYSTEM' NOT NULL,
    PRIMARY KEY (product_code, warehouse_type)
);


CREATE table if not EXISTS supplier (
	id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    code VARCHAR(50),
    name VARCHAR(255),
    contact VARCHAR(255),
    status VARCHAR(20),
    created_at timestamptz(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) DEFAULT 'SYSTEM' NOT NULL
);