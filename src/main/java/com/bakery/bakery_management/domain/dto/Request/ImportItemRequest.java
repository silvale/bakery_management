package com.bakery.bakery_management.domain.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ImportItemRequest {

    @NotBlank
    private String productCode;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotBlank
    private String unitCode;

    @NotBlank
    private String lotNumber; // String ID

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiryDate;

    private String code; // Có thể null -> lấy Default Price
}