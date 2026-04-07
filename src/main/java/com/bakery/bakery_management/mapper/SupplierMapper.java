package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.request.SupplierRequest;
import com.bakery.bakery_management.domain.dto.response.SupplierResponse;
import com.bakery.bakery_management.domain.entity.Supplier;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface SupplierMapper extends AdminBaseMapper<SupplierRequest, SupplierResponse, Supplier> {
}
