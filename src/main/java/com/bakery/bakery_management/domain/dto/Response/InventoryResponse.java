package com.bakery.bakery_management.domain.dto.Response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse extends BaseResponse {
    private String referenceId;
    private LocalDateTime importedAt;
    private WarehouseType warehouseType;
    private TransactionType transactionType;
    private ReferenceType referenceType;
    private Integer totalItems;
    private String message;
}
