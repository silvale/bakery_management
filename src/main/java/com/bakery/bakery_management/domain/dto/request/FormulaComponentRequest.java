package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FormulaComponentRequest {

    private UUID formulaId;

    private String productCode;

    private BigDecimal quantity;

    private String unitCode;

    private String note;
}
