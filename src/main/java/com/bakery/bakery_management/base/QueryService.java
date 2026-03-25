package com.bakery.bakery_management.base;

import com.bakery.bakery_management.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QueryService<RES> {
    RES getById(UUID id);
    Page<RES> getList(Pageable pageable);
}
