package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class FormulaRequest {

    private UUID id;

    private String productCode; // FINISHED

    private UUID formulaId;

    private Integer formulaVersion;

    private BigDecimal lossRate;

    private String componentType;

    private String description;

    private List<FormulaComponentRequest> components;
}
