package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface ProductPriceMapper extends AdminBaseMapper<ProductPriceRequest, ProductPriceResponse, ProductPrice> {
    @Mapping(source = "isDefault", target = "isDefault")      // Cẩn thận với kiểu boolean (is/get)
    ProductPriceResponse toResponse(ProductPrice entity);
}
