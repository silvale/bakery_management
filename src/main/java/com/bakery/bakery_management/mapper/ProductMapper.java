package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface ProductMapper extends AdminBaseMapper<ProductRequest, ProductResponse, Product> {


    @Mapping(target = "unit", expression = "java(context.getUnit(entity.getUnitCode()))")
    ProductResponse toResponse(Product entity, @Context ProductLookupContext context);

    default List<ProductResponse> toResponseList(List<Product> entities, @Context ProductLookupContext context) {
        return entities.stream()
                .map(entity -> toResponse(entity, context))
                .toList();
    }
}
