package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    List<Supplier> findAllByCodeIn(List<String> codes);
    Optional<Supplier> findByCode(String uuid);
}
