package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> {
    public abstract T getId();
}
