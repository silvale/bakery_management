package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    // Tìm để cộng dồn (Phương án 1: Không dùng Lot làm key)
    @Query("""
        SELECT i FROM Inventory i 
        WHERE i.productCode = :productCode 
          AND i.warehouseType = :warehouseType 
          AND i.expiryDate IS NOT DISTINCT FROM :expiryDate
    """)
    Optional<Inventory> findByUniqueStock(String productCode, WarehouseType warehouseType, LocalDateTime expiryDate);

    List<Inventory> findByWarehouseType(WarehouseType warehouseType);

    Optional<Inventory> findByWarehouseTypeAndProductCodeAndExpiryDate(WarehouseType warehouseType, String productCode, LocalDateTime expiryDate);

    List<Inventory> findByWarehouseTypeAndProductCodeOrderByExpiryDateAsc(WarehouseType warehouseType, String productCode);

    Optional<Inventory> findByProductCodeAndWarehouseTypeAndExpiryDate(
            String productCode,
            WarehouseType warehouseType,
            LocalDateTime expiryDate
    );
}
