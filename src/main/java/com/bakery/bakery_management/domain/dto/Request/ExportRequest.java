package com.bakery.bakery_management.domain.dto.Request;

import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ExportRequest {
    @NotBlank
    private String referenceId;    // Mã phiếu xuất (ví dụ: EX-2026-001)

    @NotNull
    private WarehouseType warehouseType;

    @NotNull
    private TransactionType transactionType; // EXPORT

    @NotNull
    private ReferenceType referenceType; // Bán hàng, Xuất sang bếp, Hủy hàng...

    private String note;

    @NotEmpty
    @Valid
    private List<ExportItemRequest> items;
}
