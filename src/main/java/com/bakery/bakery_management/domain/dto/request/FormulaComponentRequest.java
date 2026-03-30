package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FormulaComponentRequest {

    private String productCode;

    private BigDecimal quantity;

    private String unitCode;

    private String note;
}
