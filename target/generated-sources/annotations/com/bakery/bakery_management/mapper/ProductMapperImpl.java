package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:32:00+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        product.setStatus( request.getStatus() );
        product.setCode( request.getCode() );
        product.setName( request.getName() );
        product.setUnitCode( request.getUnitCode() );
        product.setType( request.getType() );

        return product;
    }

    @Override
    public Product update(ProductRequest request, Product entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setName( request.getName() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setType( request.getType() );

        return entity;
    }

    @Override
    public Product patch(ProductRequest request, Product entity) {
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
        if ( request.getUnitCode() != null ) {
            entity.setUnitCode( request.getUnitCode() );
        }
        if ( request.getType() != null ) {
            entity.setType( request.getType() );
        }

        return entity;
    }

    @Override
    public Product snapshot(Product entity) {
        if ( entity == null ) {
            return null;
        }

        Product product = new Product();

        product.setStatus( entity.getStatus() );
        product.setCreatedAt( entity.getCreatedAt() );
        product.setCreatedBy( entity.getCreatedBy() );
        product.setId( entity.getId() );
        product.setUpdatedAt( entity.getUpdatedAt() );
        product.setUpdatedBy( entity.getUpdatedBy() );
        product.setCode( entity.getCode() );
        product.setName( entity.getName() );
        product.setBarcode( entity.getBarcode() );
        product.setUnitCode( entity.getUnitCode() );
        product.setType( entity.getType() );

        return product;
    }

    @Override
    public ProductResponse toResponse(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId( entity.getId() );
        productResponse.setCreatedBy( entity.getCreatedBy() );
        productResponse.setCreatedAt( entity.getCreatedAt() );
        productResponse.setUpdatedBy( entity.getUpdatedBy() );
        productResponse.setUpdatedAt( entity.getUpdatedAt() );
        productResponse.setCode( entity.getCode() );
        productResponse.setName( entity.getName() );
        productResponse.setType( entity.getType() );
        productResponse.setUnitCode( entity.getUnitCode() );
        productResponse.setStatus( entity.getStatus() );

        return productResponse;
    }

    @Override
    public Collection<ProductResponse> toResponse(Collection<Product> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<ProductResponse> collection = new ArrayList<ProductResponse>( entity.size() );
        for ( Product product : entity ) {
            collection.add( toResponse( product ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(ProductRequest request, Product entity) {
        if ( request == null ) {
            return;
        }

        entity.setStatus( request.getStatus() );
        entity.setCode( request.getCode() );
        entity.setName( request.getName() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setType( request.getType() );
    }
}
