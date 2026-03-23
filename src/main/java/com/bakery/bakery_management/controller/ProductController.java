package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public Long create(@RequestBody ProductRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ProductResponse detail(@PathVariable Long id) {
        return service.getDetail(id);
    }

    @GetMapping
    public List<ProductResponse> list() {
        return service.getList();
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody ProductRequest request) {
        service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
