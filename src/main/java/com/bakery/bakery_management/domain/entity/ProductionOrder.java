package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.BaseEntity;
import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "production_order")
@Getter
@Setter
public class ProductionOrder extends JpaEntity<UUID> {

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "planned_qty")
    private Double plannedQty;

    @Column(name = "actual_qty")
    private Double actualQty;

}
