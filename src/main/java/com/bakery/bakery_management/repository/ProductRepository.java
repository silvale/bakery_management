package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);
}
