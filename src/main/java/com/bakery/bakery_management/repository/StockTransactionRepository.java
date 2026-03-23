package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.InventoryBatch;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
}