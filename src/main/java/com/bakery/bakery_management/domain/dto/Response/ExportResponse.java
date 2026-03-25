package com.bakery.bakery_management.domain.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExportResponse {
    private String referenceId;
    private LocalDateTime importedAt; // Thời gian hệ thống ghi nhận (updated_at)
    private int totalItems;           // Tổng số mặt hàng đã xử lý
    private String status;            // Ví dụ: "SUCCESS"
    private String message;           // "Nhập kho thành công cho phiếu GR-xxx"
}

