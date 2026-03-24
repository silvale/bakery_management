package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.UnitRequest;
import com.bakery.bakery_management.domain.dto.Response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import com.bakery.bakery_management.service.UnitService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/units")
public class UnitResource extends AdminBaseResource<UnitRequest, UnitResponse, Unit> {

    private final UnitService service;

    public UnitResource(UnitService service) {
        this.service = service;
    }

    @Override
    protected AdminOperationService<UnitRequest, UnitResponse, Unit> getService() {
        return this.service;
    }

}
