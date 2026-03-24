package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class Inventory extends JpaEntityAuditable<UUID> {

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_type")
    private WarehouseType warehouseType;

    @Column(precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "lot_number")
    private String lotNumber; // Liên kết tới Import Ticket ID

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    // Helper check hết hạn nhanh cho Hải
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }

}
