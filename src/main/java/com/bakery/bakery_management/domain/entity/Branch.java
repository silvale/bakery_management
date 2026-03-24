package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "branch")
@Getter
@Setter
public class Branch extends JpaEntity<UUID> {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;
}
