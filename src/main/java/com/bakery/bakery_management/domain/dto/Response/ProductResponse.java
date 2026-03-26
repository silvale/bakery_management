package com.bakery.bakery_management.domain.dto.Response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponse extends BaseResponse {

    private String code;
    private String name;
    private ProductType type;
    private ReferenceResponse unit;
    private StatusCode status;
    private List<ProductPriceResponse> prices;
    private BigDecimal currentSalesPrice;
    private BigDecimal currentCostPrice;


}
