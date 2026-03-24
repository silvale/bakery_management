package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductMapper;
import com.bakery.bakery_management.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService extends AdminOperationService<ProductRequest, ProductResponse, Product> {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Constructor Injection
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    protected JpaRepository<Product, UUID> getRepository() {
        return this.productRepository;
    }

    @Override
    protected AdminBaseMapper<ProductRequest, ProductResponse, Product> getMapper() {
        return this.productMapper;
    }
}