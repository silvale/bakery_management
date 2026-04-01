package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.Unit;
import com.bakery.bakery_management.domain.enums.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    boolean existsByCode(String code);

    Optional<Unit> findByCode(String code);

    List<Unit> findAllByCodeIn(List<String> codes);

}
