package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_transactions")
@Getter
@Setter
public class StockTransaction extends JpaEntityAuditable<UUID> {

    private String productCode;
    private String referenceId; // Mã phiếu nhập/xuất
    private BigDecimal quantity; // Nhập (+), Xuất (-)
    private String unitCode;
    private String lotNumber;
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // IMPORT, EXPORT

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    private String note;
}
