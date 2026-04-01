package com.bakery.bakery_management.domain.enums;

public enum TransactionType {
    IMPORT,
    EXPORT,
    RETURN,
    ADJ, // Adjustment điều chỉnh tồn kho
    DISCARD,
    SALE,
    PRODUCT_OUT,
    PRODUCT_IN
}
