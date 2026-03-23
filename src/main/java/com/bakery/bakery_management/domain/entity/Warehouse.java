package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "branch")
@Getter
@Setter
public class Warehouse extends JpaEntityAuditable<Long> {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
