package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.UnitConversionRequest;
import com.bakery.bakery_management.domain.dto.response.UnitConversionResponse;
import com.bakery.bakery_management.domain.entity.UnitConversion;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface UnitConversionMapper extends AdminBaseMapper<UnitConversionRequest, UnitConversionResponse, UnitConversion> {
}
