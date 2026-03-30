CREATE TABLE inventory (
	id uuid NOT NULL,
	reference_id varchar(255),
	warehouse_type varchar(255),
	process_date timestamp(6),
	product_code varchar(255),
	unit_code varchar(255),
	quantity numeric(38, 2),
	expiry_date timestamp(6),
	status varchar(255),
	created_at timestamptz(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) DEFAULT 'SYSTEM'::character varying NOT NULL,
	updated_at timestamptz(6) NULL,
	updated_by varchar(255) NULL,
	CONSTRAINT inventory_pkey PRIMARY KEY (id),
	CONSTRAINT inventory_product_code_fkey FOREIGN KEY (product_code) REFERENCES product(code),
	CONSTRAINT inventory_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit(code),
	CONSTRAINT inventory_warehouse_type_fkey FOREIGN KEY (warehouse_type) REFERENCES warehouse(code)
);

CREATE TABLE stock_transactions (
	id uuid NOT NULL,
	reference_id varchar(255) NULL,
	warehouse_type varchar(255) NULL,
	process_date timestamp(6),
	reference_type varchar(255) NOT NULL,
	transaction_type varchar(255) NOT NULL,
	product_code varchar(255) NULL,
	quantity numeric(38, 2) NULL,
	unit_code varchar(255) NULL,
	note varchar(255) NULL,
	expiry_date timestamp(6) NULL,
	created_at timestamptz(6) NOT NULL,
	created_by varchar(255) NOT NULL,
	status varchar(255) NULL,
	updated_at timestamptz(6) NULL,
	updated_by varchar(255) NULL,
	CONSTRAINT stock_transactions_pkey PRIMARY KEY (id),
	CONSTRAINT stock_transactions_product_code_fkey FOREIGN KEY (product_code) REFERENCES product(code),
	CONSTRAINT stock_transactions_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit(code),
	CONSTRAINT stock_transactions_warehouse_type_fkey FOREIGN KEY (warehouse_type) REFERENCES warehouse(code)
);

CREATE TABLE formula (
    id UUID PRIMARY KEY,

    product_code VARCHAR(50) NOT NULL, -- FINISHED

    version INTEGER NOT NULL,

    description VARCHAR(255),

    loss_rate NUMERIC(5,2), -- % hao hụt

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP,
    created_by VARCHAR(50),
    last_updated_at TIMESTAMP,
    last_updated_by VARCHAR(50),

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
    component_type VARCHAR(20), -- MAIN / SUB / PACKAGING
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    last_updated_at TIMESTAMP,
    last_updated_by VARCHAR(50),
    CONSTRAINT fk_formula_component_formula FOREIGN KEY (formula_id)
        REFERENCES formula(id),

    CONSTRAINT fk_formula_component_product FOREIGN KEY (ingredient_code)
        REFERENCES product(code)
);

ALTER TABLE public.inventory ADD process_date date NULL;