package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.enums.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);

    List<Product> findByCodeIn(Set<String> productCodes);

    List<Product> findAllByCodeInAndStatus(List<String> codes, StatusCode status);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.prices pr " +
            "WHERE p.status = 'ACTIVE' AND (pr.isDefault = true OR pr IS NULL) and pr.status = 'ACTIVE' ")
    Page<Product> findAllWithDefaultPrice(Pageable pageable);

}
