package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.FormulaComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FormulaComponentRepository extends JpaRepository<FormulaComponent, UUID> {

    List<FormulaComponent> findByFormulaId(UUID formulaId);
}