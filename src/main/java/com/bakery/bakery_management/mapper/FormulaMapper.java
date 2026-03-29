package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.request.FormulaRequest;
import com.bakery.bakery_management.domain.dto.response.FormulaResponse;
import com.bakery.bakery_management.domain.entity.Formula;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface FormulaMapper extends AdminBaseMapper<
        FormulaRequest,
        FormulaResponse,
        Formula> {

    @Override
    @Mapping(target = "product", ignore = true)
    FormulaResponse toResponse(Formula entity);
}
