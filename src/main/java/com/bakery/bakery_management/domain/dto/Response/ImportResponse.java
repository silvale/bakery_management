package com.bakery.bakery_management.domain.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ImportResponse {
    private UUID batchId;
    private BigDecimal newQuantity;
}
