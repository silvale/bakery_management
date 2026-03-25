package com.bakery.bakery_management.domain.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportItemRequest {
    private String productCode;
    private BigDecimal quantity;
    private String unitCode;
    private BigDecimal salePrice;
}
