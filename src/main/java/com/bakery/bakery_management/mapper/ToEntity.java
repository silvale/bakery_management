package com.bakery.bakery_management.mapper;


import org.mapstruct.Mapping;
import org.mapstruct.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", ignore = true)
@Mapping(target = "createdBy", ignore = true)
@Mapping(target = "updatedBy", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
public @interface ToEntity {}
