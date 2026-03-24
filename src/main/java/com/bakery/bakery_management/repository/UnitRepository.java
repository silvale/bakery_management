package com.bakery.bakery_management.repository;


import com.bakery.bakery_management.domain.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

}
