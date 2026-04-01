package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExportItemRequest {

    private String referenceId;
    private String productCode;
    private String priceCode;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private LocalDateTime expiryDate;
}
