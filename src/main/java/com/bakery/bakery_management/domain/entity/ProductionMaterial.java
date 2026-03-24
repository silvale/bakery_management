package com.bakery.bakery_management.domain.entity;


import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "production_material")
@Getter
@Setter
public class ProductionMaterial extends JpaEntity<UUID> {

    @Column(name = "production_order_id")
    private UUID productionOrderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "required_qty")
    private Double requiredQty;

    @Column(name = "issued_qty")
    private Double issuedQty;
}
