package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportItemRequest {
    private String productCode;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
}
