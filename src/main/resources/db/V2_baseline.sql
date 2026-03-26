CREATE TABLE product (
	id uuid NOT NULL,
	code varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	"type" varchar(255) NULL,
	description varchar(255) NULL,
	unit_code varchar(255) NULL,
	default_expiry_days int4 NULL,
	expiry_type varchar(255) NULL,
	fixed_expiry_date date null,
	created_at timestamptz(6) NOT NULL,
	created_by varchar(255) NOT NULL,
	status varchar(255) NULL,
	updated_at timestamptz(6) NULL,
	updated_by varchar(255) NULL,
);

CREATE TABLE product_prices (
	id uuid DEFAULT uuid_generate_v7() NOT NULL,
	code varchar(255) NOT NULL,
	product_code varchar(255) NOT NULL,
	unit_code varchar(255) NOT NULL,
	cost_price numeric(19) DEFAULT 0 NULL,
	sale_price numeric(19) DEFAULT 0 NULL,
	applied_date timestamptz DEFAULT CURRENT_TIMESTAMP NULL,
	is_default bool DEFAULT false NULL,
	status varchar(255) DEFAULT 'ACTIVE'::character varying NULL,
	created_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by varchar(255) NOT NULL,
	updated_at timestamptz NULL,
	updated_by varchar(255) NULL
);


CREATE TABLE inventory (
	id uuid NOT NULL,
	expiry_date timestamp(6) NULL,
	lot_number varchar(255) NULL,
	product_code varchar(255) NOT NULL,
	quantity numeric(38, 2) NULL,
	unit_code varchar(255) NULL,
	warehouse_type varchar(255) NULL,
	"type" varchar(255) null,
	created_at timestamptz(6) NOT NULL,
	created_by varchar(255) NOT NULL,
	status varchar(255) NULL,
	updated_at timestamptz(6) NULL,
	updated_by varchar(255) NULL,
);