package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormulaRepository extends JpaRepository<Formula, UUID> {

    List<Formula> findByProductCode(String productCode);

    Optional<Formula> findTopByProductCodeOrderByFormulaVersionDesc(String productCode);
}