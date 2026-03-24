package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Query("""
        SELECT i FROM Inventory i 
        WHERE i.productCode = :productCode 
          AND i.warehouseType = :warehouseType 
          AND i.lotNumber = :lotNumber 
          AND i.expiryDate IS NOT DISTINCT FROM :expiryDate
    """)
    Optional<Inventory> findByUniqueStock(
            @Param("productCode") String productCode,
            @Param("warehouseType") WarehouseType warehouseType,
            @Param("lotNumber") String lotNumber,
            @Param("expiryDate") LocalDateTime expiryDate
    );
}
