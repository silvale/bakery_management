package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.FormulaComponentRequest;
import com.bakery.bakery_management.domain.dto.request.FormulaRequest;
import com.bakery.bakery_management.domain.dto.response.FormulaComponentResponse;
import com.bakery.bakery_management.domain.dto.response.FormulaResponse;
import com.bakery.bakery_management.domain.entity.Formula;
import com.bakery.bakery_management.domain.entity.FormulaComponent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T13:14:39+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Eclipse Adoptium)"
)
@Component
public class FormulaMapperImpl implements FormulaMapper {

    @Override
    public Formula update(FormulaRequest request, Formula entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setId( request.getId() );
        entity.setProductCode( request.getProductCode() );
        if ( request.getFormulaVersion() != null ) {
            entity.setFormulaVersion( request.getFormulaVersion() );
        }
        entity.setDescription( request.getDescription() );
        if ( request.getLossRate() != null ) {
            entity.setLossRate( request.getLossRate().toString() );
        }
        else {
            entity.setLossRate( null );
        }
        entity.setComponentType( request.getComponentType() );
        if ( entity.getComponents() != null ) {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.getComponents().clear();
                entity.getComponents().addAll( list );
            }
            else {
                entity.setComponents( null );
            }
        }
        else {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.setComponents( list );
            }
        }

        return entity;
    }

    @Override
    public Formula patch(FormulaRequest request, Formula entity) {
        if ( request == null ) {
            return entity;
        }

        if ( request.getId() != null ) {
            entity.setId( request.getId() );
        }
        if ( request.getProductCode() != null ) {
            entity.setProductCode( request.getProductCode() );
        }
        if ( request.getFormulaVersion() != null ) {
            entity.setFormulaVersion( request.getFormulaVersion() );
        }
        if ( request.getDescription() != null ) {
            entity.setDescription( request.getDescription() );
        }
        if ( request.getLossRate() != null ) {
            entity.setLossRate( request.getLossRate().toString() );
        }
        if ( request.getComponentType() != null ) {
            entity.setComponentType( request.getComponentType() );
        }
        if ( entity.getComponents() != null ) {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.getComponents().clear();
                entity.getComponents().addAll( list );
            }
        }
        else {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.setComponents( list );
            }
        }

        return entity;
    }

    @Override
    public Formula snapshot(Formula entity) {
        if ( entity == null ) {
            return null;
        }

        Formula formula = new Formula();

        formula.setStatus( entity.getStatus() );
        formula.setCreatedAt( entity.getCreatedAt() );
        formula.setCreatedBy( entity.getCreatedBy() );
        formula.setId( entity.getId() );
        formula.setUpdatedAt( entity.getUpdatedAt() );
        formula.setUpdatedBy( entity.getUpdatedBy() );
        formula.setProductCode( entity.getProductCode() );
        formula.setFormulaVersion( entity.getFormulaVersion() );
        formula.setDescription( entity.getDescription() );
        formula.setLossRate( entity.getLossRate() );
        formula.setComponentType( entity.getComponentType() );
        formula.setActive( entity.isActive() );
        List<FormulaComponent> list = entity.getComponents();
        if ( list != null ) {
            formula.setComponents( new ArrayList<FormulaComponent>( list ) );
        }

        link( formula );

        return formula;
    }

    @Override
    public Collection<FormulaResponse> toResponse(Collection<Formula> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<FormulaResponse> collection = new ArrayList<FormulaResponse>( entity.size() );
        for ( Formula formula : entity ) {
            collection.add( toResponse( formula ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(FormulaRequest request, Formula entity) {
        if ( request == null ) {
            return;
        }

        entity.setId( request.getId() );
        entity.setProductCode( request.getProductCode() );
        if ( request.getFormulaVersion() != null ) {
            entity.setFormulaVersion( request.getFormulaVersion() );
        }
        entity.setDescription( request.getDescription() );
        if ( request.getLossRate() != null ) {
            entity.setLossRate( request.getLossRate().toString() );
        }
        else {
            entity.setLossRate( null );
        }
        entity.setComponentType( request.getComponentType() );
        if ( entity.getComponents() != null ) {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.getComponents().clear();
                entity.getComponents().addAll( list );
            }
            else {
                entity.setComponents( null );
            }
        }
        else {
            List<FormulaComponent> list = formulaComponentRequestListToFormulaComponentList( request.getComponents() );
            if ( list != null ) {
                entity.setComponents( list );
            }
        }

        link( entity );
    }

    @Override
    public FormulaResponse toResponse(Formula entity) {
        if ( entity == null ) {
            return null;
        }

        FormulaResponse formulaResponse = new FormulaResponse();

        formulaResponse.setId( entity.getId() );
        formulaResponse.setCreatedBy( entity.getCreatedBy() );
        formulaResponse.setCreatedAt( entity.getCreatedAt() );
        formulaResponse.setUpdatedBy( entity.getUpdatedBy() );
        formulaResponse.setUpdatedAt( entity.getUpdatedAt() );
        formulaResponse.setStatus( entity.getStatus() );
        formulaResponse.setFormulaVersion( entity.getFormulaVersion() );
        if ( entity.getLossRate() != null ) {
            formulaResponse.setLossRate( new BigDecimal( entity.getLossRate() ) );
        }
        formulaResponse.setComponentType( entity.getComponentType() );
        formulaResponse.setDescription( entity.getDescription() );
        formulaResponse.setComponents( formulaComponentListToFormulaComponentResponseList( entity.getComponents() ) );

        return formulaResponse;
    }

    @Override
    public Formula toEntity(FormulaRequest request) {
        if ( request == null ) {
            return null;
        }

        Formula formula = new Formula();

        formula.setComponents( formulaComponentRequestListToFormulaComponentList( request.getComponents() ) );
        formula.setId( request.getId() );
        formula.setProductCode( request.getProductCode() );
        if ( request.getFormulaVersion() != null ) {
            formula.setFormulaVersion( request.getFormulaVersion() );
        }
        formula.setDescription( request.getDescription() );
        if ( request.getLossRate() != null ) {
            formula.setLossRate( request.getLossRate().toString() );
        }
        formula.setComponentType( request.getComponentType() );

        link( formula );

        return formula;
    }

    protected FormulaComponent formulaComponentRequestToFormulaComponent(FormulaComponentRequest formulaComponentRequest) {
        if ( formulaComponentRequest == null ) {
            return null;
        }

        FormulaComponent formulaComponent = new FormulaComponent();

        formulaComponent.setProductCode( formulaComponentRequest.getProductCode() );
        formulaComponent.setQuantity( formulaComponentRequest.getQuantity() );
        formulaComponent.setUnitCode( formulaComponentRequest.getUnitCode() );
        formulaComponent.setNote( formulaComponentRequest.getNote() );

        return formulaComponent;
    }

    protected List<FormulaComponent> formulaComponentRequestListToFormulaComponentList(List<FormulaComponentRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<FormulaComponent> list1 = new ArrayList<FormulaComponent>( list.size() );
        for ( FormulaComponentRequest formulaComponentRequest : list ) {
            list1.add( formulaComponentRequestToFormulaComponent( formulaComponentRequest ) );
        }

        return list1;
    }

    protected FormulaComponentResponse formulaComponentToFormulaComponentResponse(FormulaComponent formulaComponent) {
        if ( formulaComponent == null ) {
            return null;
        }

        FormulaComponentResponse formulaComponentResponse = new FormulaComponentResponse();

        formulaComponentResponse.setId( formulaComponent.getId() );
        formulaComponentResponse.setCreatedBy( formulaComponent.getCreatedBy() );
        formulaComponentResponse.setCreatedAt( formulaComponent.getCreatedAt() );
        formulaComponentResponse.setUpdatedBy( formulaComponent.getUpdatedBy() );
        formulaComponentResponse.setUpdatedAt( formulaComponent.getUpdatedAt() );
        formulaComponentResponse.setStatus( formulaComponent.getStatus() );
        formulaComponentResponse.setQuantity( formulaComponent.getQuantity() );
        formulaComponentResponse.setNote( formulaComponent.getNote() );

        return formulaComponentResponse;
    }

    protected List<FormulaComponentResponse> formulaComponentListToFormulaComponentResponseList(List<FormulaComponent> list) {
        if ( list == null ) {
            return null;
        }

        List<FormulaComponentResponse> list1 = new ArrayList<FormulaComponentResponse>( list.size() );
        for ( FormulaComponent formulaComponent : list ) {
            list1.add( formulaComponentToFormulaComponentResponse( formulaComponent ) );
        }

        return list1;
    }
}
