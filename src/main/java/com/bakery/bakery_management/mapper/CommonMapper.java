package com.bakery.bakery_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueMappingStrategy = org.mapstruct.NullValueMappingStrategy.RETURN_NULL)
public interface CommonMapper {

}
