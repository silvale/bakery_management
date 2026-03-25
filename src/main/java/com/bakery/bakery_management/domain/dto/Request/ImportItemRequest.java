package com.bakery.bakery_management.domain.dto.Request;

import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ImportItemRequest {

    private String productCode;
    private BigDecimal quantity;
    private String unitCode;
    private String lotNumber;

    // Control Expiry từ FE
    private ExpiryInputType expiryType;
    private Integer manualExpiryDays;
    private LocalDateTime manualExpiryDate;

    // Price sync
    private BigDecimal costPrice;

}