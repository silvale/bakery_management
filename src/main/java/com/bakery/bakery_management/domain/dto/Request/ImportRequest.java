package com.bakery.bakery_management.domain.dto.Request;

import com.bakery.bakery_management.domain.enums.ReferenceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ImportRequest {

    private String productCode;
    private String warehouseCode;
    private String batchNo;
    private BigDecimal quantity;
    private LocalDate expiryDate;

    private ReferenceType referenceType;
    private String referenceId;
}