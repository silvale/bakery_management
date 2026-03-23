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
    date = "2026-03-23T22:44:53+0700",
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
        product.setType( request.getType() );
        product.setUnitCode( request.getUnitCode() );
        product.setShelfLifeDays( request.getShelfLifeDays() );

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
        entity.setType( request.getType() );
        entity.setUnitCode( request.getUnitCode() );
        entity.setShelfLifeDays( request.getShelfLifeDays() );

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
        if ( request.getType() != null ) {
            entity.setType( request.getType() );
        }
        if ( request.getUnitCode() != null ) {
            entity.setUnitCode( request.getUnitCode() );
        }
        if ( request.getShelfLifeDays() != null ) {
            entity.setShelfLifeDays( request.getShelfLifeDays() );
        }

        return entity;
    }

    @Override
    public Product snapshot(Product entity) {
        if ( entity == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( entity.getId() );
        product.setStatus( entity.getStatus() );
        product.setCreatedAt( entity.getCreatedAt() );
        product.setCreatedBy( entity.getCreatedBy() );
        product.setUpdatedAt( entity.getUpdatedAt() );
        product.setUpdatedBy( entity.getUpdatedBy() );
        product.setCode( entity.getCode() );
        product.setName( entity.getName() );
        product.setType( entity.getType() );
        product.setUnitCode( entity.getUnitCode() );
        product.setShelfLifeDays( entity.getShelfLifeDays() );

        return product;
    }

    @Override
    public ProductResponse toResponse(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setCreatedBy( entity.getCreatedBy() );
        productResponse.setCreatedAt( entity.getCreatedAt() );
        productResponse.setUpdatedBy( entity.getUpdatedBy() );
        productResponse.setUpdatedAt( entity.getUpdatedAt() );
        productResponse.setId( entity.getId() );
        productResponse.setCode( entity.getCode() );
        productResponse.setName( entity.getName() );
        productResponse.setType( entity.getType() );
        productResponse.setUnitCode( entity.getUnitCode() );
        productResponse.setShelfLifeDays( entity.getShelfLifeDays() );
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
}
