package com.bakery.bakery_management.domain.dto.Request;

import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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
    private Integer shelfLifeDays;
    private StatusCode status;
}
