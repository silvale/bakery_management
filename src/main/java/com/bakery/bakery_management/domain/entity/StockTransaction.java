package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "stock_transaction")
@Getter
@Setter
public class StockTransaction extends JpaEntityAuditable<UUID> {

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "warehouse_code")
    private String warehouseCode;

    @Column(name = "batch_id")
    private UUID batchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "reference_type")
    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Column(name = "reference_id")
    private String referenceId;
}
