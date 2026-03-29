package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductPriceResponse extends BaseResponse {

    private String code;
    private String productCode;
    private String unitCode;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private Boolean isDefault;
    private LocalDateTime appliedDate;
    private StatusCode status;
}
