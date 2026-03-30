package com.bakery.bakery_management.repository;

import com.bakery.bakery_management.domain.entity.FormulaComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FormulaComponentRepository extends JpaRepository<FormulaComponent, UUID> {

    List<FormulaComponent> findByFormulaId(UUID formulaId);

    @Modifying
    @Query("""
                DELETE FROM FormulaComponent c
                WHERE c.formula.id = :formulaId
            """)
    void deleteByFormulaId(@Param("formulaId") UUID formulaId);
}