package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.FormulaComponentRequest;
import com.bakery.bakery_management.domain.dto.response.FormulaComponentResponse;
import com.bakery.bakery_management.domain.entity.FormulaComponent;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T21:11:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class FormulaComponentMapperImpl implements FormulaComponentMapper {

    @Override
    public FormulaComponent toEntity(FormulaComponentRequest request) {
        if ( request == null ) {
            return null;
        }

        FormulaComponent formulaComponent = new FormulaComponent();

        formulaComponent.setProductCode( request.getProductCode() );
        formulaComponent.setQuantity( request.getQuantity() );
        formulaComponent.setUnitCode( request.getUnitCode() );
        formulaComponent.setNote( request.getNote() );

        return formulaComponent;
    }

    @Override
    public FormulaComponent update(FormulaComponentRequest request, FormulaComponent entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setProductCode( request.getProductCode() );
        entity.setQuantity( request.getQuantity() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setNote( request.getNote() );

        return entity;
    }

    @Override
    public FormulaComponent patch(FormulaComponentRequest request, FormulaComponent entity) {
        if ( request == null ) {
            return entity;
        }

        if ( request.getProductCode() != null ) {
            entity.setProductCode( request.getProductCode() );
        }
        if ( request.getQuantity() != null ) {
            entity.setQuantity( request.getQuantity() );
        }
        if ( request.getUnitCode() != null ) {
            entity.setUnitCode( request.getUnitCode() );
        }
        if ( request.getNote() != null ) {
            entity.setNote( request.getNote() );
        }

        return entity;
    }

    @Override
    public FormulaComponent snapshot(FormulaComponent entity) {
        if ( entity == null ) {
            return null;
        }

        FormulaComponent formulaComponent = new FormulaComponent();

        formulaComponent.setStatus( entity.getStatus() );
        formulaComponent.setCreatedAt( entity.getCreatedAt() );
        formulaComponent.setCreatedBy( entity.getCreatedBy() );
        formulaComponent.setId( entity.getId() );
        formulaComponent.setUpdatedAt( entity.getUpdatedAt() );
        formulaComponent.setUpdatedBy( entity.getUpdatedBy() );
        formulaComponent.setFormula( entity.getFormula() );
        formulaComponent.setProductCode( entity.getProductCode() );
        formulaComponent.setQuantity( entity.getQuantity() );
        formulaComponent.setUnitCode( entity.getUnitCode() );
        formulaComponent.setNote( entity.getNote() );

        return formulaComponent;
    }

    @Override
    public Collection<FormulaComponentResponse> toResponse(Collection<FormulaComponent> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<FormulaComponentResponse> collection = new ArrayList<FormulaComponentResponse>( entity.size() );
        for ( FormulaComponent formulaComponent : entity ) {
            collection.add( toResponse( formulaComponent ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(FormulaComponentRequest request, FormulaComponent entity) {
        if ( request == null ) {
            return;
        }

        entity.setProductCode( request.getProductCode() );
        entity.setQuantity( request.getQuantity() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setNote( request.getNote() );
    }

    @Override
    public FormulaComponentResponse toResponse(FormulaComponent entity) {
        if ( entity == null ) {
            return null;
        }

        FormulaComponentResponse formulaComponentResponse = new FormulaComponentResponse();

        formulaComponentResponse.setId( entity.getId() );
        formulaComponentResponse.setCreatedBy( entity.getCreatedBy() );
        formulaComponentResponse.setCreatedAt( entity.getCreatedAt() );
        formulaComponentResponse.setUpdatedBy( entity.getUpdatedBy() );
        formulaComponentResponse.setUpdatedAt( entity.getUpdatedAt() );
        formulaComponentResponse.setStatus( entity.getStatus() );
        formulaComponentResponse.setQuantity( entity.getQuantity() );
        formulaComponentResponse.setNote( entity.getNote() );

        return formulaComponentResponse;
    }
}
