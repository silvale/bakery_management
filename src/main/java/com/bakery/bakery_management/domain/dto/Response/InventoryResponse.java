package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponse extends BaseResponse {

    private ReferenceResponse product;
    private WarehouseType warehouseType;
    private BigDecimal quantity;
    private ReferenceResponse unit;
    private String referenceId;
    private LocalDateTime expiryDate;

}