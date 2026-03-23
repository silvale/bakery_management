package com.bakery.bakery_management.domain.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ImportResponse {
    private Long batchId;
    private BigDecimal newQuantity;
}
