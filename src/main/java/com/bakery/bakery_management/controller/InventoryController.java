package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ImportRequest;
import com.bakery.bakery_management.domain.dto.Request.InventoryRequest;
import com.bakery.bakery_management.domain.dto.Response.ImportResponse;
import com.bakery.bakery_management.domain.dto.Response.InventoryResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController extends AdminBaseResource<InventoryRequest, InventoryResponse, Inventory> {

    private final InventoryService inventoryService;

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> internalImport(@RequestBody @Valid ImportRequest request) {
        return ResponseEntity.ok(inventoryService.processImport(request));
    }

    @Override
    protected AdminOperationService<InventoryRequest, InventoryResponse, Inventory> getService() {
        return inventoryService;
    }
}