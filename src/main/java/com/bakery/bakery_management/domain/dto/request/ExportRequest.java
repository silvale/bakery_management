package com.bakery.bakery_management.domain.dto.request;

import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDateTime processDate;

    private String note;

    @NotEmpty
    private List<ExportItemRequest> items;
}
