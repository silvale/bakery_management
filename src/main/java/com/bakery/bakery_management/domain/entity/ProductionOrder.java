package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "production_order")
@Getter
@Setter
public class ProductionOrder extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "planned_qty")
    private Double plannedQty;

    @Column(name = "actual_qty")
    private Double actualQty;

    @Column(name = "status")
    private String status;

}
