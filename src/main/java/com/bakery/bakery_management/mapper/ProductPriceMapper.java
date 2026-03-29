package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface ProductPriceMapper extends AdminBaseMapper<ProductPriceRequest, ProductPriceResponse, ProductPrice> {

    @Mapping(source = "isDefault", target = "isDefault")
    ProductPriceResponse toResponse(ProductPrice entity);
}
