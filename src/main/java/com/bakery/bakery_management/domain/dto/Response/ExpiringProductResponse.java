package com.bakery.bakery_management.domain.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ExpiringProductResponse {
    private String productCode;
    private String productName;
    private String unitCode;
    private BigDecimal remainingQuantity;
    private LocalDateTime expiryDate;
    private Long daysRemaining; // Số ngày còn lại
    private String warehouseType;
}
