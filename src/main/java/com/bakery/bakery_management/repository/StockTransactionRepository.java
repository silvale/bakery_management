package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, UUID> {

    List<StockTransaction> findByWarehouseTypeAndProductCode(WarehouseType warehouseType, String productCode);

    List<StockTransaction> findByWarehouseTypeAndReferenceId(WarehouseType warehouseType, String referenceId);

    List<StockTransaction> findByCreatedAtBetween(Instant start, Instant end);
}
