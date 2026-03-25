package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-25T17:20:14+0700",
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
        product.setExpiryType( request.getExpiryType() );
        product.setPrices( productPriceRequestListToProductPriceList( request.getPrices() ) );

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
        entity.setExpiryType( request.getExpiryType() );
        if ( entity.getPrices() != null ) {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.getPrices().clear();
                entity.getPrices().addAll( list );
            }
            else {
                entity.setPrices( null );
            }
        }
        else {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.setPrices( list );
            }
        }

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
        if ( request.getExpiryType() != null ) {
            entity.setExpiryType( request.getExpiryType() );
        }
        if ( entity.getPrices() != null ) {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.getPrices().clear();
                entity.getPrices().addAll( list );
            }
        }
        else {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.setPrices( list );
            }
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
        product.setUnitCode( entity.getUnitCode() );
        product.setType( entity.getType() );
        product.setExpiryType( entity.getExpiryType() );
        product.setDefaultExpiryDays( entity.getDefaultExpiryDays() );
        product.setFixedExpiryDate( entity.getFixedExpiryDate() );
        List<ProductPrice> list = entity.getPrices();
        if ( list != null ) {
            product.setPrices( new ArrayList<ProductPrice>( list ) );
        }

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
        productResponse.setPrices( productPriceListToProductPriceResponseList( entity.getPrices() ) );

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
        entity.setExpiryType( request.getExpiryType() );
        if ( entity.getPrices() != null ) {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.getPrices().clear();
                entity.getPrices().addAll( list );
            }
            else {
                entity.setPrices( null );
            }
        }
        else {
            List<ProductPrice> list = productPriceRequestListToProductPriceList( request.getPrices() );
            if ( list != null ) {
                entity.setPrices( list );
            }
        }
    }

    protected ProductPrice productPriceRequestToProductPrice(ProductPriceRequest productPriceRequest) {
        if ( productPriceRequest == null ) {
            return null;
        }

        ProductPrice productPrice = new ProductPrice();

        productPrice.setStatus( productPriceRequest.getStatus() );
        productPrice.setCode( productPriceRequest.getCode() );
        productPrice.setProductCode( productPriceRequest.getProductCode() );
        productPrice.setUnitCode( productPriceRequest.getUnitCode() );
        productPrice.setCostPrice( productPriceRequest.getCostPrice() );
        productPrice.setSalePrice( productPriceRequest.getSalePrice() );
        productPrice.setAppliedDate( productPriceRequest.getAppliedDate() );

        return productPrice;
    }

    protected List<ProductPrice> productPriceRequestListToProductPriceList(List<ProductPriceRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductPrice> list1 = new ArrayList<ProductPrice>( list.size() );
        for ( ProductPriceRequest productPriceRequest : list ) {
            list1.add( productPriceRequestToProductPrice( productPriceRequest ) );
        }

        return list1;
    }

    protected ProductPriceResponse productPriceToProductPriceResponse(ProductPrice productPrice) {
        if ( productPrice == null ) {
            return null;
        }

        ProductPriceResponse.ProductPriceResponseBuilder productPriceResponse = ProductPriceResponse.builder();

        productPriceResponse.code( productPrice.getCode() );
        productPriceResponse.productCode( productPrice.getProductCode() );
        productPriceResponse.unitCode( productPrice.getUnitCode() );
        productPriceResponse.costPrice( productPrice.getCostPrice() );
        productPriceResponse.salePrice( productPrice.getSalePrice() );
        productPriceResponse.isDefault( productPrice.getIsDefault() );
        productPriceResponse.appliedDate( productPrice.getAppliedDate() );
        productPriceResponse.status( productPrice.getStatus() );

        return productPriceResponse.build();
    }

    protected List<ProductPriceResponse> productPriceListToProductPriceResponseList(List<ProductPrice> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductPriceResponse> list1 = new ArrayList<ProductPriceResponse>( list.size() );
        for ( ProductPrice productPrice : list ) {
            list1.add( productPriceToProductPriceResponse( productPrice ) );
        }

        return list1;
    }
}
