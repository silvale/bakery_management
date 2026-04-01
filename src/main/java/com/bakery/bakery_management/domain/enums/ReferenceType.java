package com.bakery.bakery_management.domain.enums;

public enum ReferenceType {
    DAILY, // Daily Xuất hàng ngày.
    EXPORT_TO_KITCHEN,
    EXPORT_TO_STORE,
    RETURN_TO_STORAGE,
    DAMAGED,
    RETURN_TO_SUPPLIER,
    INVALID_LIST,        // hàng không đúng danh sách
    EXPIRED,             // hàng hết hạn
    INVALID_QUALITY,     // hàng không đúng chất lượng
    INVALID_QUANTITY,    // hàng sai khối lượng
    DAMAGE,                // khác
    OTHER                // khác
    }
