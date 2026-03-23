package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends JpaEntityAuditable<Long> {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;
}
