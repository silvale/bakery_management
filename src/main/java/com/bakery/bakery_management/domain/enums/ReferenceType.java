package com.bakery.bakery_management.domain.enums;

public enum ReferenceType {
    // -- Use for export
    DAILY, // Daily Xuất hàng ngày.
    EXPORT_TO_KITCHEN,
    RETURN_TO_STORAGE,
    SALE, // Daily Xuất hàng ngày.
    // FOR RETURN FROM KIT CHEN
    INVALID_LIST,        // hàng không đúng danh sách
    EXPIRED,             // hàng hết hạn
    INVALID_QUALITY,     // hàng không đúng chất lượng
    INVALID_QUANTITY,    // hàng sai khối lượng
    OTHER                // khác
    }
