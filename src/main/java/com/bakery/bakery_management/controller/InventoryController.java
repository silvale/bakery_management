package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.request.ExportRequest;
import com.bakery.bakery_management.domain.dto.request.ImportRequest;
import com.bakery.bakery_management.domain.dto.request.InventoryRequest;
import com.bakery.bakery_management.domain.dto.response.ExportResponse;
import com.bakery.bakery_management.domain.dto.response.ImportResponse;
import com.bakery.bakery_management.domain.dto.response.InventoryResponse;
import com.bakery.bakery_management.domain.dto.response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController extends AdminBaseResource<InventoryRequest, InventoryResponse, Inventory> {

    private final InventoryService inventoryService;

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> internalImport(@RequestBody @Valid ImportRequest request) {
        return ResponseEntity.ok(inventoryService.processImport(request));
    }

    @PostMapping("/export")
    public ResponseEntity<ExportResponse> internalExport(@RequestBody @Valid ExportRequest exportRequest) {
        return ResponseEntity.ok(inventoryService.processExport(exportRequest));
    }

    @GetMapping("/warehouse/{warehouse}")
    public PageResult<InventoryResponse> getListInventory(@PathVariable String warehouse, @ParameterObject Pageable pageable) {
        List<InventoryResponse> responseList = inventoryService.getListInventoryByType(warehouse);
        return PageResult.ofPage(new PageImpl<>(responseList, pageable, responseList.size()));
    }

    @GetMapping("/available-product")
    public PageResult<ProductResponse> getAvailableProduct(@ParameterObject Pageable pageable) {
        List<ProductResponse> responseList = inventoryService.getAvailableProduct();
        return PageResult.ofPage(new PageImpl<>(responseList, pageable, responseList.size()));
    }

    @Override
    protected AdminOperationService<InventoryRequest, InventoryResponse, Inventory> getService() {
        return inventoryService;
    }
}