package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.UnitConversionRequest;
import com.bakery.bakery_management.domain.dto.response.UnitConversionResponse;
import com.bakery.bakery_management.domain.entity.UnitConversion;
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
public class UnitConversionMapperImpl implements UnitConversionMapper {

    @Override
    public UnitConversion toEntity(UnitConversionRequest request) {
        if ( request == null ) {
            return null;
        }

        UnitConversion unitConversion = new UnitConversion();

        return unitConversion;
    }

    @Override
    public UnitConversion update(UnitConversionRequest request, UnitConversion entity) {
        if ( request == null ) {
            return entity;
        }

        return entity;
    }

    @Override
    public UnitConversion patch(UnitConversionRequest request, UnitConversion entity) {
        if ( request == null ) {
            return entity;
        }

        return entity;
    }

    @Override
    public UnitConversion snapshot(UnitConversion entity) {
        if ( entity == null ) {
            return null;
        }

        UnitConversion unitConversion = new UnitConversion();

        unitConversion.setStatus( entity.getStatus() );
        unitConversion.setCreatedAt( entity.getCreatedAt() );
        unitConversion.setCreatedBy( entity.getCreatedBy() );
        unitConversion.setId( entity.getId() );
        unitConversion.setFromUnitCode( entity.getFromUnitCode() );
        unitConversion.setToUnitCode( entity.getToUnitCode() );
        unitConversion.setRatio( entity.getRatio() );

        return unitConversion;
    }

    @Override
    public UnitConversionResponse toResponse(UnitConversion entity) {
        if ( entity == null ) {
            return null;
        }

        UnitConversionResponse unitConversionResponse = new UnitConversionResponse();

        unitConversionResponse.setId( entity.getId() );
        unitConversionResponse.setCreatedBy( entity.getCreatedBy() );
        unitConversionResponse.setCreatedAt( entity.getCreatedAt() );
        unitConversionResponse.setStatus( entity.getStatus() );
        unitConversionResponse.setFromUnitCode( entity.getFromUnitCode() );
        unitConversionResponse.setToUnitCode( entity.getToUnitCode() );
        unitConversionResponse.setRatio( entity.getRatio() );

        return unitConversionResponse;
    }

    @Override
    public Collection<UnitConversionResponse> toResponse(Collection<UnitConversion> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<UnitConversionResponse> collection = new ArrayList<UnitConversionResponse>( entity.size() );
        for ( UnitConversion unitConversion : entity ) {
            collection.add( toResponse( unitConversion ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(UnitConversionRequest request, UnitConversion entity) {
        if ( request == null ) {
            return;
        }
    }
}
