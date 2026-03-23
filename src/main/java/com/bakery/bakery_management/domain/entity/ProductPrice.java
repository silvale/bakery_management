package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.domain.enums.PriceType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_price",
        indexes = {
                @Index(name = "idx_price_product", columnList = "product_code"),
                @Index(name = "idx_price_active", columnList = "product_code, price_type, effective_to")
        })
public class ProductPrice extends JpaEntityAuditable<Long> {

    @Column(name = "product_code")
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type")
    private PriceType priceType;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "status")
    private String status; // ACTIVE / INACTIVE
}