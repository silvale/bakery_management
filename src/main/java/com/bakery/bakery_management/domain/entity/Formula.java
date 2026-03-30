package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DynamicUpdate
@Entity
@Getter
@Setter
@Table(name = "formula")
public class Formula extends JpaEntityAuditable<UUID> {

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "formula_id")
    private UUID formulaId;

    @Column(name = "formula_version")
    private int formulaVersion;

    private String description;

    @Column(name = "loss_rate")
    private String lossRate;

    @Column(name = "component_type")
    private String componentType;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "formula", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FormulaComponent> components = new ArrayList<>();
}
