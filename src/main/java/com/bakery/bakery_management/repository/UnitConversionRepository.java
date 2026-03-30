package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.UnitConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnitConversionRepository extends JpaRepository<UnitConversion, UUID> {

    Optional<UnitConversion> findByFromUnitCodeAndToUnitCode(String fromUnit, String toUnit);


}
