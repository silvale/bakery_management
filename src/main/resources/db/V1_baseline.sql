-- =========================
-- UNIT (đơn vị)
-- =========================
CREATE TABLE unit (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(50),
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE unit IS 'Đơn vị đo (kg, g, ml, cái...)';

INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(1, 'g', 'Gram', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(2, 'kg', 'Kg', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(3, 'c', 'Cái', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(4, 'h', 'Hộp', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(5, 'l', 'Lít', '2026-03-21 14:17:04.067', 'System', NULL, NULL);
INSERT INTO public.unit
(id, code, "name", created_at, created_by, updated_by, updated_at)
VALUES(6, 'ml', 'Mili lít', '2026-03-21 14:17:04.067', 'System', NULL, NULL);

-- =========================
-- UNIT CONVERSION (quy đổi đơn vị)
-- =========================
CREATE TABLE unit_conversion (
    id BIGSERIAL PRIMARY KEY,
    from_unit_code varchar(20) REFERENCES unit(code),
    to_unit_code varchar(20) REFERENCES unit(code),
    multiplier NUMERIC(18,6),
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);
-- =========================
-- PRODUCT (sản phẩm/nguyên liệu)
-- =========================
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(255),
    type VARCHAR(20),
    unit_code varchar(20) REFERENCES unit(code),
    shelf_life_days INT,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE product IS 'Danh sách sản phẩm: nguyên liệu, bán thành phẩm, bánh';
COMMENT ON COLUMN product.type IS 'RAW: nguyên liệu, SEMI: bán thành phẩm, FINISHED: thành phẩm';
COMMENT ON COLUMN product.shelf_life_days IS 'Số ngày sử dụng (hạn dùng)';

CREATE TABLE product_price (
    id BIGSERIAL PRIMARY KEY,
    product_code VARCHAR(50) REFERENCES product(code),

    price_type VARCHAR(20), -- COST / SALE

    price NUMERIC(18,4),

    effective_from TIMESTAMP,
    effective_to TIMESTAMP,

    status VARCHAR(10), -- ACTIVE / INACTIVE

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

CREATE TABLE branch (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE branch IS 'Chi nhánh cửa hàng';
COMMENT ON COLUMN branch.code IS 'Mã chi nhánh';
COMMENT ON COLUMN branch.name IS 'Tên chi nhánh';
COMMENT ON COLUMN branch.address IS 'Địa chỉ';

INSERT INTO public.branch
(id, code, "name", address, status, created_at, created_by, updated_by, updated_at)
VALUES(1, 'QT', 'Quang Trung', '72 Quang Trung', 'A', '2026-03-21 15:22:36.767', 'System', NULL, NULL);

-- =========================
-- WAREHOUSE (kho)
-- =========================
CREATE TABLE warehouse (
    id BIGSERIAL PRIMARY KEY,
    branch_code varchar(50) REFERENCES branch(code),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE warehouse IS 'Kho (kho hàng hoặc kho bếp)';
COMMENT ON COLUMN warehouse.type IS 'Loại kho: STORE (kho hàng), KITCHEN (kho bếp)';

INSERT INTO public.warehouse
(id, branch_code, code, "name", "type", status, created_at, created_by, updated_by, updated_at)
VALUES(1, 'QT', 'S', 'Kho hàng', 'S', 'A', '2026-03-21 15:23:48.254', 'System', NULL, NULL);
INSERT INTO public.warehouse
(id, branch_code, code, "name", "type", status, created_at, created_by, updated_by, updated_at)
VALUES(2, 'QT', 'K', 'Kho Bếp', 'K', 'A', '2026-03-21 15:23:48.258', 'System', NULL, NULL);

-- =========================
-- INVENTORY BATCH (tồn kho theo lô)
-- =========================
CREATE TABLE inventory_batch (
    id BIGSERIAL PRIMARY KEY,
    product_code varchar(50) REFERENCES product(code),
    warehouse_code varchar(50) REFERENCES warehouse(code),
    batch_no VARCHAR(100),
    quantity NUMERIC(18,4),
    expiry_date DATE,
    status varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE inventory_batch IS 'Tồn kho theo từng lô (có hạn sử dụng)';
COMMENT ON COLUMN inventory_batch.expiry_date IS 'Ngày hết hạn';

CREATE INDEX idx_inventory_batch_fefo
ON inventory_batch(product_code, warehouse_code, expiry_date);

CREATE INDEX idx_inventory_batch_qty
ON inventory_batch(quantity);

ALTER TABLE inventory_batch
ADD CONSTRAINT uq_batch UNIQUE (product_code, warehouse_code, batch_no);


-- =========================
-- STOCK TRANSACTION (lịch sử xuất nhập)
-- =========================
CREATE TABLE stock_transaction (
    id BIGSERIAL PRIMARY KEY,
    product_code VARCHAR(20) REFERENCES product(code),
    warehouse_code VARCHAR(20) REFERENCES warehouse(code),
    batch_id BIGINT REFERENCES inventory_batch(id),
    transaction_type VARCHAR(20),
    quantity NUMERIC(18,4),
    reference_type VARCHAR(50),
    reference_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE stock_transaction IS 'Lịch sử xuất nhập kho';
COMMENT ON COLUMN stock_transaction.transaction_type IS 'IN: nhập, OUT: xuất, TRANSFER: chuyển kho';
COMMENT ON COLUMN stock_transaction.reference_type IS 'Nguồn phát sinh: PURCHASE, PRODUCTION...';

