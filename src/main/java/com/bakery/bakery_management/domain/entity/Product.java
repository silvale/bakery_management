package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends JpaEntityAuditable<UUID> {

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private String barcode;

    @Column(name = "unit_code")
    private String unitCode;

    @Enumerated(EnumType.STRING)
    private ProductType type;
}
