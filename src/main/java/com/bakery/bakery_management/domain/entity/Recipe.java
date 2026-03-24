package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.BaseEntity;
import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Recipe extends JpaEntity<UUID> {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "name")
    private String name;

}
