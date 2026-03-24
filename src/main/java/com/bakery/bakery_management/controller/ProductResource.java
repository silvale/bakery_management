package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductResource extends AdminBaseResource<ProductRequest, ProductResponse, Product> {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected AdminOperationService<ProductRequest, ProductResponse, Product> getService() {
        return this.productService;
    }

}
