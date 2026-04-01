package com.bakery.bakery_management.domain.dto.request;

import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ImportItemRequest {

    private String productCode;
    private String priceCode;
    private BigDecimal quantity;
    private String unitCode;
    private String referenceId;

    // Control Expiry từ FE
    private ExpiryInputType expiryType;
    private Integer manualExpiryDays;
    private LocalDateTime manualExpiryDate;

    // Price sync
    private BigDecimal costPrice;

}