package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
}