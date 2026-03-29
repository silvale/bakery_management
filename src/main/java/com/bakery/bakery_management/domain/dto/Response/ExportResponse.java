package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExportResponse {
    private String referenceId;
    private WarehouseType warehouseType;
    private TransactionType transactionType;
    private ReferenceType referenceType;
    private LocalDateTime importedAt; // Thời gian hệ thống ghi nhận (updated_at)
    private int totalItems;           // Tổng số mặt hàng đã xử lý
    private String status;            // Ví dụ: "SUCCESS"
    private String message;           // "Nhập kho thành công cho phiếu GR-xxx"
}

