package com.bakery.bakery_management.domain.dto.request;

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
public class ImportRequest {

    @NotBlank
    private String referenceId;    // Mã phiếu nhập (ví dụ: GR-2026-001)

    @NotNull
    private WarehouseType warehouseType;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private ReferenceType referenceType;

    private String note;

    @NotEmpty
    @Valid // Đảm bảo validate từng item bên dưới
    private List<ImportItemRequest> items;
}