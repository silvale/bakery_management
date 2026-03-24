package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "unit")
@Getter
@Setter
public class Unit extends JpaEntity<UUID> {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
