package com.bakery.bakery_management.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CommonMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CentralMapperConfig {}
