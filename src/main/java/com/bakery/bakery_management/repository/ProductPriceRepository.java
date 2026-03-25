package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.enums.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {

    Optional<ProductPrice> findByCode(String code);

    Optional<ProductPrice> findByProductCodeAndUnitCodeAndIsDefaultTrue(String productCode, String unitCode);

    @Query("SELECT p FROM ProductPrice p " +
            "WHERE p.productCode IN :codes " +
            "AND p.isDefault = true " +
            "AND p.status = 'ACTIVE'")
    List<ProductPrice> findAllDefaultPrices(@Param("codes") List<String> codes);

    List<ProductPrice> findByProductCodeAndStatusOrderByAppliedDateDesc(String productCode, StatusCode status);
}
