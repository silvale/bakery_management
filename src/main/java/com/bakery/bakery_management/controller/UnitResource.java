package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.UnitRequest;
import com.bakery.bakery_management.domain.dto.response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import com.bakery.bakery_management.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitResource extends AdminBaseResource<UnitRequest, UnitResponse, Unit> {

    private final UnitService unitService;

    @Override
    protected AdminOperationService<UnitRequest, UnitResponse, Unit> getService() {
        return this.unitService;
    }
}
