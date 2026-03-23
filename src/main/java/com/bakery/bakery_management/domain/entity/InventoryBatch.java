package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.domain.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "inventory_batch",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "product_code", "warehouse_code", "batch_no"
        }))
public class InventoryBatch extends JpaEntityAuditable<Long> {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "warehouse_code")
    private String warehouseCode;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "batch_status")
    private BatchStatus batchStatus;

}