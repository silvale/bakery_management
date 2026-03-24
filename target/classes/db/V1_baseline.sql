CREATE OR REPLACE FUNCTION uuid_generate_v7()
RETURNS uuid
AS $$
DECLARE
    unix_ts_ms bytea;
    uuid_bytes bytea;
BEGIN
    -- 1. Lấy timestamp hiện tại tính bằng milliseconds (6 bytes)
    unix_ts_ms := decode(lpad(to_hex(floor(extract(epoch from clock_timestamp()) * 1000)::bigint), 12, '0'), 'hex');

    -- 2. Tạo 10 bytes ngẫu nhiên còn lại
    uuid_bytes := unix_ts_ms || gen_random_bytes(10);

    -- 3. Thiết lập Version 7 (4 bits đầu của byte thứ 7 là 0111 -> 0x7x)
    uuid_bytes := set_byte(uuid_bytes, 6, (get_byte(uuid_bytes, 6) & 15) | 112);

    -- 4. Thiết lập Variant (2 bits đầu của byte thứ 9 là 10 -> 0x8x)
    uuid_bytes := set_byte(uuid_bytes, 8, (get_byte(uuid_bytes, 8) & 63) | 128);

    RETURN encode(uuid_bytes, 'hex')::uuid;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================
-- UNIT (đơn vị)
-- =========================
CREATE TABLE unit (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7() ,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(50),
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE unit IS 'Đơn vị đo (kg, g, ml, cái...)';

INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('g', 'Gram', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('kg', 'Kg', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('c', 'Cái', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('h', 'Hộp', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('l', 'Lít', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(code, "name", created_at, created_by, updated_by, updated_at)
VALUES('ml', 'Mili lít', '2026-03-21 14:17:04.067', 'System', NULL, NULL);

-- =========================
-- UNIT CONVERSION (quy đổi đơn vị)
-- =========================
CREATE TABLE unit_conversion (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    from_unit_code varchar(20) REFERENCES unit(code),
    to_unit_code varchar(20) REFERENCES unit(code),
    multiplier NUMERIC(18,6),
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);
-- =========================
-- PRODUCT (sản phẩm/nguyên liệu)
-- =========================
CREATE TABLE product (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
   	code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(100),
    unit_code VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

-- 2. Bảng Product (Master Data)
CREATE TABLE products (
    id UUID PRIMARY KEY, -- Sử dụng UUID v7 từ Java
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(100),
    unit_code VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_by VARCHAR(100),
    created_date TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

CREATE TABLE inventory (
    id UUID PRIMARY KEY,
    product_code VARCHAR(50) NOT NULL,
    warehouse_type VARCHAR(20) NOT NULL,
    quantity DECIMAL(19, 3) NOT NULL, -- Chính xác đến gram/ml
    unit_code VARCHAR(20) NOT NULL,
    lot_number VARCHAR(20) NOT NULL, -- ID của Import Ticket
    expiry_date TIMESTAMP WITHOUT TIME ZONE, -- Hạn dùng đến từng giờ
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_by VARCHAR(100),
    created_date TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by VARCHAR(100),
    last_modified_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE stock_transactions (
    id UUID PRIMARY KEY,
    product_code VARCHAR(50) NOT NULL,
    warehouse_type VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- IMPORT, EXPORT, ADJUST
    reference_type VARCHAR(30) NOT NULL,   -- PURCHASE_IMPORT, INTERNAL_TRANSFER...
    quantity DECIMAL(19, 3) NOT NULL,
    unit_code VARCHAR(20) NOT NULL,
    lot_number VARCHAR(50),
    reference_id VARCHAR(50),
    note TEXT,

    -- Audit fields style Hải Lê
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_by VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE INDEX idx_stock_tx_composite ON stock_transactions(product_code, warehouse_type, reference_type);

-- 4. Bảng Product Prices (History)
CREATE TABLE product_prices (
    id UUID PRIMARY KEY,
    price_code VARCHAR(50) UNIQUE NOT NULL, -- User tự đặt hoặc hệ thống gen: P-001, P-002
    product_code VARCHAR(50) NOT NULL,
    unit_code VARCHAR(20) NOT NULL,
    cost_price DECIMAL(19, 0) NOT NULL,
    sale_price DECIMAL(19, 0) NOT NULL,
    applied_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- Audit fields style Hải Lê
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_by VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- Index để tối ưu tìm kiếm
CREATE INDEX idx_inventory_product_code ON inventory(product_code);
CREATE INDEX idx_inventory_expiry ON inventory(expiry_date);
CREATE INDEX idx_product_prices_lookup ON product_prices(product_code, unit_code);


CREATE TABLE branch (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE branch IS 'Chi nhánh cửa hàng';
COMMENT ON COLUMN branch.code IS 'Mã chi nhánh';
COMMENT ON COLUMN branch.name IS 'Tên chi nhánh';
COMMENT ON COLUMN branch.address IS 'Địa chỉ';

INSERT INTO public.branch
(code, "name", address, status, created_at, created_by, updated_by, updated_at)
VALUES('QT', 'Quang Trung', '72 Quang Trung', 'A', '2026-03-21 15:22:36.767', 'System', NULL, NULL);

-- =========================
-- WAREHOUSE (kho)
-- =========================
CREATE TABLE warehouse (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    branch_code varchar(50) REFERENCES branch(code),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'SYSTEM',
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE warehouse IS 'Kho (kho hàng hoặc kho bếp)';
COMMENT ON COLUMN warehouse.type IS 'Loại kho: STORE (kho hàng), KITCHEN (kho bếp)';

INSERT INTO public.warehouse
(branch_code, code, "name", "type", status, created_at, created_by, updated_by, updated_at)
VALUES('QT', 'S', 'Kho hàng', 'S', 'A', '2026-03-21 15:23:48.254', 'System', NULL, NULL);
INSERT INTO public.warehouse
(branch_code, code, "name", "type", status, created_at, created_by, updated_by, updated_at)
VALUES('QT', 'K', 'Kho Bếp', 'K', 'A', '2026-03-21 15:23:48.258', 'System', NULL, NULL);

