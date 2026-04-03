package com.bakery.bakery_management.domain.enums;

public enum ReferenceType {
    //    IMPORT
    DAILY, // Daily Xuất hàng ngày.
    ADJUSTMENT, // Điều chỉnh

    // EXPORT
    EXPORT_TO_KITCHEN,
    EXPORT_TO_STORE,

    //    RETURN
    RETURN_TO_STORAGE,      // Trả vef
    RETURN_TO_SUPPLIER, // Trả hàng nhà sản xuất
    INVALID_LIST,        // hàng không đúng danh sách
    INVALID_QUALITY,     // hàng không đúng chất lượng
    INVALID_QUANTITY,   // hàng sai khối lượng

    // DISCARD
    DAMAGED,            // Hàng hư hỏng
    EXPIRED,             // hàng hết hạn

    // ADJUSTMENT
    INCREATE,
    DECREATE
}
