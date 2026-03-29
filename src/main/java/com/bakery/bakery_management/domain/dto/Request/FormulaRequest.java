package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FormulaRequest {

    private String productCode; // FINISHED

    private Integer version;

    private BigDecimal lossRate;

    private String componentType;

    private String description;

    private List<FormulaComponentRequest> components;
}
