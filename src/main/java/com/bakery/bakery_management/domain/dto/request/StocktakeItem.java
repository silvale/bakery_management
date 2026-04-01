package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StocktakeItem {
    String productCode;
    LocalDateTime expiryDate;
    BigDecimal remainingQty;
}
