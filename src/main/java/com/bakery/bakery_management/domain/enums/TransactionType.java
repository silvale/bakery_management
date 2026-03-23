package com.bakery.bakery_management.domain.enums;

public enum TransactionType {
    I, // IMPORT -> Hàng vào
    E, // EXPORT -> Hàng ra
    PI, // PRODUCTION IN : Nhập thành phẩm sau sản xuất
    PO, // PRODUCTION OUTPUT : Xuất nguyên liệu để sản xuất
    R, // RETURN : Trả hàng
    ADJ // ADJUSTMENT : Điều chỉnh tồn kho (manual)
}
