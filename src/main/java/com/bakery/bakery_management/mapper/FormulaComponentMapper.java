package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.FormulaComponentRequest;
import com.bakery.bakery_management.domain.dto.response.FormulaComponentResponse;
import com.bakery.bakery_management.domain.entity.FormulaComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface FormulaComponentMapper extends AdminBaseMapper<
        FormulaComponentRequest,
        FormulaComponentResponse,
        FormulaComponent> {

    @Override
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unit", ignore = true)
    FormulaComponentResponse toResponse(FormulaComponent entity);
}
