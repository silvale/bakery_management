package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier extends JpaEntity<UUID> {

    private String code;

    private String name;

    private String contact;
}
