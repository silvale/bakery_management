package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "production_material")
@Getter
@Setter
public class ProductionMaterial extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "production_order_id")
    private Long productionOrderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "required_qty")
    private Double requiredQty;

    @Column(name = "issued_qty")
    private Double issuedQty;
}
