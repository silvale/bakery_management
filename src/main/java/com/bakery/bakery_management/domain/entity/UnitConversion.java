package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "unit_conversion")
@Getter
@Setter
public class UnitConversion extends JpaEntity<UUID> {

    @Column(name = "from_unit_code")
    private String fromUnitCode;

    @Column(name = "to_unit_code")
    private String toUnitCode;

    @Column(name = "ratio")
    private BigDecimal ratio;

}
