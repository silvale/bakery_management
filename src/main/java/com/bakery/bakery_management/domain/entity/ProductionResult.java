package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "production_result")
@Getter
@Setter
public class ProductionResult extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "production_order_id")
    private Long productionOrderId;

    @Column(name = "actual_qty")
    private Double actualQty;
}
