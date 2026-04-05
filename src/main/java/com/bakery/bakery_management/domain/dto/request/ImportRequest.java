package com.bakery.bakery_management.domain.dto.request;

import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImportRequest {

    @NotBlank
    private String referenceId;

    private LocalDateTime processDate;

    @NotNull
    private WarehouseType warehouseType;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private ReferenceType referenceType;

    private String note;

    @NotEmpty
    @Valid
    private List<ImportItemRequest> items;
}