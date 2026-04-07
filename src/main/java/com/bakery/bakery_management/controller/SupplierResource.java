package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.SupplierRequest;
import com.bakery.bakery_management.domain.dto.response.SupplierResponse;
import com.bakery.bakery_management.domain.entity.Supplier;
import com.bakery.bakery_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierResource extends AdminBaseResource<SupplierRequest, SupplierResponse, Supplier> {

    private final SupplierService supplierService;

    @Override
    protected AdminOperationService<SupplierRequest, SupplierResponse, Supplier> getService() {
        return this.supplierService;
    }
}
