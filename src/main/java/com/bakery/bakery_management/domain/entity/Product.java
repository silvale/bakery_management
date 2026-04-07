package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends JpaEntityAuditable<UUID> {

    private String code;

    private String name;

    private String unitCode;

    @Column(name = "supplier_code")
    private String supplierCode;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ExpiryInputType expiryType;

    @Column(name = "default_expiry_days")
    private Integer defaultExpiryDays;

    private LocalDate fixedExpiryDate;

    @Column(name = "warning_quantity")
    private Integer warningQuantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @OrderBy("appliedDate DESC")
    private List<ProductPrice> prices = new ArrayList<>();
}
