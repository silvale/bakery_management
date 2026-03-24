package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.BaseEntity;
import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "production_result")
@Getter
@Setter
public class ProductionResult extends JpaEntity<UUID> {

    @Column(name = "production_order_id")
    private UUID productionOrderId;

    @Column(name = "actual_qty")
    private Double actualQty;
}
