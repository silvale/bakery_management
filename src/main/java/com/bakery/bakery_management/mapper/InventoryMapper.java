package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.Request.InventoryRequest;
import com.bakery.bakery_management.domain.dto.Response.InventoryResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface InventoryMapper extends AdminBaseMapper<InventoryRequest, InventoryResponse, Inventory> {
}
