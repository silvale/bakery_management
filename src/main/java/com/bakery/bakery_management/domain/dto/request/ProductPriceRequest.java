package com.bakery.bakery_management.domain.dto.request;

import com.bakery.bakery_management.domain.enums.StatusCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceRequest {

    private String code;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productCode;

    private String unitCode;

    @PositiveOrZero
    private BigDecimal costPrice;

    @PositiveOrZero
    private BigDecimal salePrice;

    private boolean isDefault;

    private LocalDateTime appliedDate;

    private StatusCode status = StatusCode.ACTIVE;
}
