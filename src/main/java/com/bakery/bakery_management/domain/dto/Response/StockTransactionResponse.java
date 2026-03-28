package com.bakery.bakery_management.domain.dto.Response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class StockTransactionResponse extends BaseResponse {

    private String referenceId;
    private WarehouseType warehouseType;
    private TransactionType transactionType;
    private ReferenceType referenceType;
    private ReferenceResponse product;
    private ReferenceResponse unit;
    private BigDecimal quantity;
    private LocalDateTime expiryDate;
    private String note;
}
