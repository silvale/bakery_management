package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.enums.ProductType;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface ProductMapper extends AdminBaseMapper<ProductRequest, ProductResponse, Product> {

//    @Override
//    @Mapping(target = "type", expression = "java(entity.getType())")
//    ProductResponse toResponse(Product entity);

    default ProductType map(String type) {
        if (type == null) return null;

        return ProductType.valueOf(type.toUpperCase());
    }
}
