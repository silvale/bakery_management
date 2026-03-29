package com.bakery.bakery_management.domain.dto.request;

import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

    private String code;
    private String name;
    private ProductType type;
    private String unitCode;
    private StatusCode status;
    private ExpiryInputType expiryType;

    private Integer defaultExpiryDays;

    private LocalDate fixedExpiryDate;

    List<ProductPriceRequest> prices;

    private FormulaRequest formula;
}
