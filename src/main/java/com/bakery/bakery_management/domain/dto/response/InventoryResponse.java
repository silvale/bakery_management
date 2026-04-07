package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponse extends BaseResponse {

    private String referenceId;
    private LocalDate processDate;
    private WarehouseType warehouseType;
    private ReferenceResponse product;
    private ReferenceResponse unit;
    private String priceCodeDefault;
    private BigDecimal currentSalesPrice;
    private BigDecimal currentCostPrice;
    private BigDecimal quantity;
    private LocalDateTime expiryDate;
    private Integer warningQuantity;
}