package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.service.ProductPriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-prices")
public class ProductPriceResource extends AdminBaseResource<ProductPriceRequest, ProductPriceResponse, ProductPrice> {

    private final ProductPriceService ProductPriceService;

    public ProductPriceResource(ProductPriceService ProductPriceService) {
        this.ProductPriceService = ProductPriceService;
    }

    @Override
    protected AdminOperationService<ProductPriceRequest, ProductPriceResponse, ProductPrice> getService() {
        return this.ProductPriceService;
    }

}
