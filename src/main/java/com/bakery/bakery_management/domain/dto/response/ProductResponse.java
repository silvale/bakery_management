package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProductResponse extends BaseResponse {

    private String code;
    private String name;
    private ProductType type;
    private ReferenceResponse unit;
    private ReferenceResponse supplier;
    private StatusCode status;
    private String priceCodeDefault;
    private BigDecimal currentSalesPrice;
    private BigDecimal currentCostPrice;
    private ExpiryInputType expiryType;
    private Integer defaultExpiryDays;
    private LocalDate fixedExpiryDate;
    private List<ProductPriceResponse> prices;
    private FormulaResponse formula;
    private BigDecimal availableQuantity;
    private Integer warningQuantity;


}
