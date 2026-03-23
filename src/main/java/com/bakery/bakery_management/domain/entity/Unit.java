package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unit")
@Getter
@Setter
public class Unit extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
