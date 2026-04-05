package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.StockTransactionRequest;
import com.bakery.bakery_management.domain.dto.response.StockTransactionResponse;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T13:14:38+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Eclipse Adoptium)"
)
@Component
public class StockTransactionMapperImpl implements StockTransactionMapper {

    @Override
    public StockTransaction toEntity(StockTransactionRequest request) {
        if ( request == null ) {
            return null;
        }

        StockTransaction stockTransaction = new StockTransaction();

        return stockTransaction;
    }

    @Override
    public StockTransaction update(StockTransactionRequest request, StockTransaction entity) {
        if ( request == null ) {
            return entity;
        }

        return entity;
    }

    @Override
    public StockTransaction patch(StockTransactionRequest request, StockTransaction entity) {
        if ( request == null ) {
            return entity;
        }

        return entity;
    }

    @Override
    public StockTransaction snapshot(StockTransaction entity) {
        if ( entity == null ) {
            return null;
        }

        StockTransaction stockTransaction = new StockTransaction();

        stockTransaction.setStatus( entity.getStatus() );
        stockTransaction.setCreatedAt( entity.getCreatedAt() );
        stockTransaction.setCreatedBy( entity.getCreatedBy() );
        stockTransaction.setId( entity.getId() );
        stockTransaction.setUpdatedAt( entity.getUpdatedAt() );
        stockTransaction.setUpdatedBy( entity.getUpdatedBy() );
        stockTransaction.setProductCode( entity.getProductCode() );
        stockTransaction.setReferenceId( entity.getReferenceId() );
        stockTransaction.setProcessDate( entity.getProcessDate() );
        stockTransaction.setQuantity( entity.getQuantity() );
        stockTransaction.setUnitCode( entity.getUnitCode() );
        stockTransaction.setExpiryDate( entity.getExpiryDate() );
        stockTransaction.setWarehouseType( entity.getWarehouseType() );
        stockTransaction.setTransactionType( entity.getTransactionType() );
        stockTransaction.setReferenceType( entity.getReferenceType() );
        stockTransaction.setNote( entity.getNote() );

        return stockTransaction;
    }

    @Override
    public StockTransactionResponse toResponse(StockTransaction entity) {
        if ( entity == null ) {
            return null;
        }

        StockTransactionResponse stockTransactionResponse = new StockTransactionResponse();

        stockTransactionResponse.setId( entity.getId() );
        stockTransactionResponse.setCreatedBy( entity.getCreatedBy() );
        stockTransactionResponse.setCreatedAt( entity.getCreatedAt() );
        stockTransactionResponse.setUpdatedBy( entity.getUpdatedBy() );
        stockTransactionResponse.setUpdatedAt( entity.getUpdatedAt() );
        stockTransactionResponse.setStatus( entity.getStatus() );
        stockTransactionResponse.setReferenceId( entity.getReferenceId() );
        stockTransactionResponse.setWarehouseType( entity.getWarehouseType() );
        stockTransactionResponse.setTransactionType( entity.getTransactionType() );
        stockTransactionResponse.setReferenceType( entity.getReferenceType() );
        stockTransactionResponse.setQuantity( entity.getQuantity() );
        stockTransactionResponse.setExpiryDate( entity.getExpiryDate() );
        stockTransactionResponse.setNote( entity.getNote() );

        return stockTransactionResponse;
    }

    @Override
    public Collection<StockTransactionResponse> toResponse(Collection<StockTransaction> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<StockTransactionResponse> collection = new ArrayList<StockTransactionResponse>( entity.size() );
        for ( StockTransaction stockTransaction : entity ) {
            collection.add( toResponse( stockTransaction ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(StockTransactionRequest request, StockTransaction entity) {
        if ( request == null ) {
            return;
        }
    }
}
