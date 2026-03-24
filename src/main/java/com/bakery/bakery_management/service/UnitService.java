package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.UnitRequest;
import com.bakery.bakery_management.domain.dto.Response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.UnitMapper;
import com.bakery.bakery_management.repository.UnitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnitService extends AdminOperationService<UnitRequest, UnitResponse, Unit> {

    private final UnitRepository repository;
    private final UnitMapper mapper;

    // Constructor Injection
    public UnitService(UnitRepository repository, UnitMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Unit, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<UnitRequest, UnitResponse, Unit> getMapper() {
        return this.mapper;
    }
}