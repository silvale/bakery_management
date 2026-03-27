package com.bakery.bakery_management.mapper;



import com.bakery.bakery_management.base.ListingSummary;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;

public interface AdminBaseMapper<D, R, E> {

    @BeanMapping(qualifiedBy = ToEntity.class)
    E toEntity(D request);

    @BeanMapping(qualifiedBy = ToEntity.class)
    E update(D request, @MappingTarget E entity);

    @BeanMapping(
            qualifiedBy = ToEntity.class,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    E patch(D request, @MappingTarget E entity);

    E snapshot(E entity);

    @BeanMapping(qualifiedBy = ToEntity.class)
    R toResponse(E entity);

    Collection<R> toResponse(Collection<E> entity);

    default R toResponse(E entity, ListingSummary summary) {
        return this.toResponse(entity);
    }

    void updateEntity(D request, @MappingTarget E entity);
}