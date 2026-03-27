package com.bakery.bakery_management.domain.dto.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportItemRequest {
    private String productCode;
    private BigDecimal quantity;
    private String unitCode;
    private BigDecimal salePrice;
}
