package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.UnitRequest;
import com.bakery.bakery_management.domain.dto.response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-07T23:24:44+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Eclipse Adoptium)"
)
@Component
public class UnitMapperImpl implements UnitMapper {

    @Override
    public Unit toEntity(UnitRequest request) {
        if ( request == null ) {
            return null;
        }

        Unit unit = new Unit();

        unit.setStatus( request.getStatus() );
        unit.setCode( request.getCode() );
        unit.setName( request.getName() );

        return unit;
    }

    @Override
    public Unit update(UnitRequest request, Unit entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setName( request.getName() );

        return entity;
    }

    @Override
    public Unit patch(UnitRequest request, Unit entity) {
        if ( request == null ) {
            return entity;
        }

        if ( request.getStatus() != null ) {
            entity.setStatus( request.getStatus() );
        }
        if ( request.getCode() != null ) {
            entity.setCode( request.getCode() );
        }
        if ( request.getName() != null ) {
            entity.setName( request.getName() );
        }

        return entity;
    }

    @Override
    public Unit snapshot(Unit entity) {
        if ( entity == null ) {
            return null;
        }

        Unit unit = new Unit();

        unit.setStatus( entity.getStatus() );
        unit.setCreatedAt( entity.getCreatedAt() );
        unit.setCreatedBy( entity.getCreatedBy() );
        unit.setId( entity.getId() );
        unit.setCode( entity.getCode() );
        unit.setName( entity.getName() );

        return unit;
    }

    @Override
    public UnitResponse toResponse(Unit entity) {
        if ( entity == null ) {
            return null;
        }

        UnitResponse unitResponse = new UnitResponse();

        unitResponse.setId( entity.getId() );
        unitResponse.setCreatedBy( entity.getCreatedBy() );
        unitResponse.setCreatedAt( entity.getCreatedAt() );
        unitResponse.setStatus( entity.getStatus() );
        unitResponse.setCode( entity.getCode() );
        unitResponse.setName( entity.getName() );

        return unitResponse;
    }

    @Override
    public Collection<UnitResponse> toResponse(Collection<Unit> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<UnitResponse> collection = new ArrayList<UnitResponse>( entity.size() );
        for ( Unit unit : entity ) {
            collection.add( toResponse( unit ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(UnitRequest request, Unit entity) {
        if ( request == null ) {
            return;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setName( request.getName() );
    }
}
