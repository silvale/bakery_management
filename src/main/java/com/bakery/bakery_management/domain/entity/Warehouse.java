package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "branch")
@Getter
@Setter
public class Warehouse extends JpaEntityAuditable<UUID> {

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private WarehouseType type;
}
