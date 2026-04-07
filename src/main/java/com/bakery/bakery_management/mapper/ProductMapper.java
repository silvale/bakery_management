package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.request.ProductRequest;
import com.bakery.bakery_management.domain.dto.response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface ProductMapper extends AdminBaseMapper<ProductRequest, ProductResponse, Product> {
}
