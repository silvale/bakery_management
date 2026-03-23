package com.bakery.bakery_management.service;


import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;

public interface ProductService
        extends AdminBaseService<ProductRequest, ProductResponse, Long> {

    void update(Long id, ProductRequest request);

}
