package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class Inventory extends JpaEntityAuditable<UUID> {

    private String productCode;

    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType;

    private BigDecimal quantity;

    private String unitCode;

    @Column(name = "reference_id")
    private String referenceId;

    private LocalDateTime expiryDate;

    @Column(name = "process_date")
    private LocalDate processDate;


    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }

}
