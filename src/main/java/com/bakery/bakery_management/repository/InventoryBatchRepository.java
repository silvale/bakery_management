package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.InventoryBatch;
import com.bakery.bakery_management.domain.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b FROM InventoryBatch b
        WHERE b.productCode = :productCode
        AND b.warehouseCode = :warehouseCode
        AND b.batchNo = :batchNo
    """)
    Optional<InventoryBatch> findForUpdate(
            String productCode,
            String warehouseCode,
            String batchNo
    );
}