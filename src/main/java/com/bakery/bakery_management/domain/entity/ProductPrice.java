package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.bakery.bakery_management.base.JpaEntityAuditable;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
public class ProductPrice extends JpaEntityAuditable<UUID> {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "cost_price", precision = 19, scale = 0)
    private BigDecimal costPrice; // Giá nhập/vốn

    @Column(name = "sale_price", precision = 19, scale = 0)
    private BigDecimal salePrice; // Giá bán ra

    @Column(name = "is_default")
    private boolean isDefault = false;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;
}
