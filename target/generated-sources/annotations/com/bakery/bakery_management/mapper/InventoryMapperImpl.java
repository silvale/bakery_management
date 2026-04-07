package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.request.InventoryRequest;
import com.bakery.bakery_management.domain.dto.response.InventoryResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-08T00:07:27+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(InventoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Inventory inventory = new Inventory();

        inventory.setWarehouseType( request.getWarehouseType() );
        inventory.setReferenceId( request.getReferenceId() );

        return inventory;
    }

    @Override
    public Inventory update(InventoryRequest request, Inventory entity) {
        if ( request == null ) {
            return entity;
        }

        entity.setWarehouseType( request.getWarehouseType() );
        entity.setReferenceId( request.getReferenceId() );

        return entity;
    }

    @Override
    public Inventory patch(InventoryRequest request, Inventory entity) {
        if ( request == null ) {
            return entity;
        }

        if ( request.getWarehouseType() != null ) {
            entity.setWarehouseType( request.getWarehouseType() );
        }
        if ( request.getReferenceId() != null ) {
            entity.setReferenceId( request.getReferenceId() );
        }

        return entity;
    }

    @Override
    public Inventory snapshot(Inventory entity) {
        if ( entity == null ) {
            return null;
        }

        Inventory inventory = new Inventory();

        inventory.setStatus( entity.getStatus() );
        inventory.setCreatedAt( entity.getCreatedAt() );
        inventory.setCreatedBy( entity.getCreatedBy() );
        inventory.setId( entity.getId() );
        inventory.setUpdatedAt( entity.getUpdatedAt() );
        inventory.setUpdatedBy( entity.getUpdatedBy() );
        inventory.setProductCode( entity.getProductCode() );
        inventory.setWarehouseType( entity.getWarehouseType() );
        inventory.setQuantity( entity.getQuantity() );
        inventory.setUnitCode( entity.getUnitCode() );
        inventory.setReferenceId( entity.getReferenceId() );
        inventory.setExpiryDate( entity.getExpiryDate() );
        inventory.setProcessDate( entity.getProcessDate() );

        return inventory;
    }

    @Override
    public Collection<InventoryResponse> toResponse(Collection<Inventory> entity) {
        if ( entity == null ) {
            return null;
        }

        Collection<InventoryResponse> collection = new ArrayList<InventoryResponse>( entity.size() );
        for ( Inventory inventory : entity ) {
            collection.add( toResponse( inventory ) );
        }

        return collection;
    }

    @Override
    public void updateEntity(InventoryRequest request, Inventory entity) {
        if ( request == null ) {
            return;
        }

        entity.setWarehouseType( request.getWarehouseType() );
        entity.setReferenceId( request.getReferenceId() );
    }

    @Override
    public InventoryResponse toResponse(Inventory entity) {
        if ( entity == null ) {
            return null;
        }

        InventoryResponse inventoryResponse = new InventoryResponse();

        inventoryResponse.setId( entity.getId() );
        inventoryResponse.setCreatedBy( entity.getCreatedBy() );
        inventoryResponse.setCreatedAt( entity.getCreatedAt() );
        inventoryResponse.setUpdatedBy( entity.getUpdatedBy() );
        inventoryResponse.setUpdatedAt( entity.getUpdatedAt() );
        inventoryResponse.setStatus( entity.getStatus() );
        inventoryResponse.setReferenceId( entity.getReferenceId() );
        inventoryResponse.setProcessDate( entity.getProcessDate() );
        inventoryResponse.setWarehouseType( entity.getWarehouseType() );
        inventoryResponse.setQuantity( entity.getQuantity() );
        inventoryResponse.setExpiryDate( entity.getExpiryDate() );

        return inventoryResponse;
    }
}
