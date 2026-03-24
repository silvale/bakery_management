package com.bakery.bakery_management.base;

import com.bakery.bakery_management.mapper.AdminBaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public abstract class AdminEntitySupportService<REQ, RES, E> {
    protected abstract JpaRepository<E, UUID> getRepository();

    protected abstract AdminBaseMapper<REQ, RES, E> getMapper();
}
