package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T13:14:39+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Eclipse Adoptium)"
)
@Component
public class ProductPriceMapperImpl implements ProductPriceMapper {

    @Override
    public ProductPrice toEntity(ProductPriceRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductPrice productPrice = new ProductPrice();

        productPrice.setStatus( request.getStatus() );
        productPrice.setCode( request.getCode() );
        productPrice.setProductCode( request.getProductCode() );
        productPrice.setUnitCode( request.getUnitCode() );
        productPrice.setCostPrice( request.getCostPrice() );
        productPrice.setSalePrice( request.getSalePrice() );
        productPrice.setAppliedDate( request.getAppliedDate() );

        return productPrice;
    }

    @Override
    public ProductPrice update(ProductPriceRequest request, ProductPrice entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setProductCode( request.getProductCode() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setCostPrice( request.getCostPrice() );
        entity.setSalePrice( request.getSalePrice() );
        entity.setAppliedDate( request.getAppliedDate() );

        return entity;
    }

    @Override
    public ProductPrice patch(ProductPriceRequest request, ProductPrice entity) {
        if ( request == null ) {
            return entity;
        }

        if ( request.getStatus() != null ) {
            entity.setStatus( request.getStatus() );
        }
        if ( request.getCode() != null ) {
            entity.setCode( request.getCode() );
        }
        if ( request.getProductCode() != null ) {
            entity.setProductCode( request.getProductCode() );
        }
        if ( request.getUnitCode() != null ) {
            entity.setUnitCode( request.getUnitCode() );
        }
        if ( request.getCostPrice() != null ) {
            entity.setCostPrice( request.getCostPrice() );
        }
        if ( request.getSalePrice() != null ) {
            entity.setSalePrice( request.getSalePrice() );
        }
        if ( request.getAppliedDate() != null ) {
            entity.setAppliedDate( request.getAppliedDate() );
        }

        return entity;
    }

    @Override
    public ProductPrice snapshot(ProductPrice entity) {
        if ( entity == null ) {
            return null;
        }

        ProductPrice productPrice = new ProductPrice();

        productPrice.setStatus( entity.getStatus() );
        productPrice.setCreatedAt( entity.getCreatedAt() );
        productPrice.setCreatedBy( entity.getCreatedBy() );
        productPrice.setId( entity.getId() );
        productPrice.setUpdatedAt( entity.getUpdatedAt() );
        productPrice.setUpdatedBy( entity.getUpdatedBy() );
        productPrice.setCode( entity.getCode() );
        productPrice.setFormulaId( entity.getFormulaId() );
        productPrice.setFormulaVersion( entity.getFormulaVersion() );
        productPrice.setProductCode( entity.getProductCode() );
        productPrice.setUnitCode( entity.getUnitCode() );
        productPrice.setCostPrice( entity.getCostPrice() );
        productPrice.setSalePrice( entity.getSalePrice() );
        productPrice.setIsDefault( entity.getIsDefault() );
        productPrice.setAppliedDate( entity.getAppliedDate() );
        productPrice.setProduct( entity.getProduct() );

        return productPrice;
    }

    @Override
    public Collection<ProductPriceResponse> toResponse(Collection<ProductPrice> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<ProductPriceResponse> collection = new ArrayList<ProductPriceResponse>( entity.size() );
        for ( ProductPrice productPrice : entity ) {
            collection.add( toResponse( productPrice ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(ProductPriceRequest request, ProductPrice entity) {
        if ( request == null ) {
            return;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setProductCode( request.getProductCode() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setCostPrice( request.getCostPrice() );
        entity.setSalePrice( request.getSalePrice() );
        entity.setAppliedDate( request.getAppliedDate() );
    }

    @Override
    public ProductPriceResponse toResponse(ProductPrice entity) {
        if ( entity == null ) {
            return null;
        }

        ProductPriceResponse productPriceResponse = new ProductPriceResponse();

        productPriceResponse.setIsDefault( entity.getIsDefault() );
        productPriceResponse.setId( entity.getId() );
        productPriceResponse.setCreatedBy( entity.getCreatedBy() );
        productPriceResponse.setCreatedAt( entity.getCreatedAt() );
        productPriceResponse.setUpdatedBy( entity.getUpdatedBy() );
        productPriceResponse.setUpdatedAt( entity.getUpdatedAt() );
        productPriceResponse.setCode( entity.getCode() );
        productPriceResponse.setProductCode( entity.getProductCode() );
        productPriceResponse.setUnitCode( entity.getUnitCode() );
        productPriceResponse.setCostPrice( entity.getCostPrice() );
        productPriceResponse.setSalePrice( entity.getSalePrice() );
        productPriceResponse.setAppliedDate( entity.getAppliedDate() );
        productPriceResponse.setStatus( entity.getStatus() );

        return productPriceResponse;
    }
}
