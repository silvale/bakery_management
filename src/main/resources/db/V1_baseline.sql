-- =========================
-- BRANCH (chi nhánh)
-- =========================
CREATE TABLE branch (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE branch IS 'Chi nhánh cửa hàng';
COMMENT ON COLUMN branch.code IS 'Mã chi nhánh';
COMMENT ON COLUMN branch.name IS 'Tên chi nhánh';
COMMENT ON COLUMN branch.address IS 'Địa chỉ';

-- =========================
-- WAREHOUSE (kho)
-- =========================
CREATE TABLE warehouse (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT REFERENCES branch(id),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE warehouse IS 'Kho (kho hàng hoặc kho bếp)';
COMMENT ON COLUMN warehouse.type IS 'Loại kho: STORE (kho hàng), KITCHEN (kho bếp)';

-- =========================
-- UNIT (đơn vị)
-- =========================
CREATE TABLE unit (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE unit IS 'Đơn vị đo (kg, g, ml, cái...)';

-- =========================
-- UNIT CONVERSION (quy đổi đơn vị)
-- =========================
CREATE TABLE unit_conversion (
    id BIGSERIAL PRIMARY KEY,
    from_unit_id BIGINT REFERENCES unit(id),
    to_unit_id BIGINT REFERENCES unit(id),
    multiplier NUMERIC(18,6),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE unit_conversion IS 'Quy đổi đơn vị (ví dụ: 1kg = 1000g)';

-- =========================
-- PRODUCT (sản phẩm/nguyên liệu)
-- =========================
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(255),
    type VARCHAR(20),
    unit_id BIGINT REFERENCES unit(id),
    shelf_life_days INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE product IS 'Danh sách sản phẩm: nguyên liệu, bán thành phẩm, bánh';
COMMENT ON COLUMN product.type IS 'RAW: nguyên liệu, SEMI: bán thành phẩm, FINISHED: thành phẩm';
COMMENT ON COLUMN product.shelf_life_days IS 'Số ngày sử dụng (hạn dùng)';

-- =========================
-- RECIPE (công thức)
-- =========================
CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(id),
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE recipe IS 'Công thức sản xuất cho 1 sản phẩm (1 công thức = 1 bánh)';

-- =========================
-- RECIPE INGREDIENT (chi tiết nguyên liệu)
-- =========================
CREATE TABLE recipe_ingredient (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT REFERENCES recipe(id),
    ingredient_product_id BIGINT REFERENCES product(id),
    quantity NUMERIC(18,4),
    unit_id BIGINT REFERENCES unit(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE recipe_ingredient IS 'Nguyên liệu cần cho công thức';
COMMENT ON COLUMN recipe_ingredient.quantity IS 'Số lượng nguyên liệu cho 1 bánh';

-- =========================
-- INVENTORY BATCH (tồn kho theo lô)
-- =========================
CREATE TABLE inventory_batch (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(id),
    warehouse_id BIGINT REFERENCES warehouse(id),
    batch_no VARCHAR(100),
    quantity NUMERIC(18,4),
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE inventory_batch IS 'Tồn kho theo từng lô (có hạn sử dụng)';
COMMENT ON COLUMN inventory_batch.expiry_date IS 'Ngày hết hạn';

-- =========================
-- STOCK TRANSACTION (lịch sử xuất nhập)
-- =========================
CREATE TABLE stock_transaction (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(id),
    warehouse_id BIGINT REFERENCES warehouse(id),
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

-- =========================
-- PRODUCTION ORDER (lệnh sản xuất)
-- =========================
CREATE TABLE production_order (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT REFERENCES branch(id),
    product_id BIGINT REFERENCES product(id),
    planned_qty NUMERIC(18,4),
    actual_qty NUMERIC(18,4),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE production_order IS 'Lệnh sản xuất bánh';
COMMENT ON COLUMN production_order.planned_qty IS 'Số lượng dự kiến';
COMMENT ON COLUMN production_order.actual_qty IS 'Số lượng thực tế';

-- =========================
-- PRODUCTION MATERIAL (nguyên liệu cần)
-- =========================
CREATE TABLE production_material (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT REFERENCES production_order(id),
    product_id BIGINT REFERENCES product(id),
    required_qty NUMERIC(18,4),
    issued_qty NUMERIC(18,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE production_material IS 'Nguyên liệu cần cho lệnh sản xuất';
COMMENT ON COLUMN production_material.required_qty IS 'Số lượng cần';
COMMENT ON COLUMN production_material.issued_qty IS 'Số lượng đã cấp';

-- =========================
-- PRODUCTION RESULT (kết quả)
-- =========================
CREATE TABLE production_result (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT REFERENCES production_order(id),
    actual_qty NUMERIC(18,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    updated_at TIMESTAMP
);

COMMENT ON TABLE production_result IS 'Kết quả sản xuất thực tế';