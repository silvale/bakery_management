package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.domain.dto.Request.ImportRequest;
import com.bakery.bakery_management.domain.dto.Response.ImportResponse;
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
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> internalImport(@RequestBody @Valid ImportRequest request) {
        return ResponseEntity.ok(inventoryService.processImport(request));
    }
}