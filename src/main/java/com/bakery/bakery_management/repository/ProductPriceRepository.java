package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {

    Optional<ProductPrice> findByCode(String code);

    Optional<ProductPrice> findByProductCodeAndUnitCodeAndIsDefaultTrue(String productCode, String unitCode);
}
