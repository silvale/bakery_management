package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_transactions")
@Getter
@Setter
public class StockTransaction extends JpaEntityAuditable<UUID> {

    private String productCode;
    private String referenceId;

    @Column(name = "process_date")
    private LocalDate processDate;
    private BigDecimal quantity;
    private String unitCode;
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    private String note;
}
