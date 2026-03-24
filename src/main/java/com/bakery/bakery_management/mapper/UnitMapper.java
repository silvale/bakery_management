package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.Request.UnitRequest;
import com.bakery.bakery_management.domain.dto.Response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface UnitMapper extends AdminBaseMapper<UnitRequest, UnitResponse, Unit> {
}
