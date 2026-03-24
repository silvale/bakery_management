package com.bakery.bakery_management.service;

import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductPriceMapper;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductPriceService extends AdminOperationService<ProductPriceRequest, ProductPriceResponse, ProductPrice> {

    private final ProductPriceMapper mapper;

    private final ProductPriceRepository repository;

    public ProductPriceService(ProductPriceMapper mapper, ProductPriceRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    protected JpaRepository<ProductPrice, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<ProductPriceRequest, ProductPriceResponse, ProductPrice> getMapper() {
        return this.mapper;
    }
}
