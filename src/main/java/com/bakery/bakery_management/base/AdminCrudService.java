package com.bakery.bakery_management.base;

import java.util.UUID;

public interface AdminCrudService<REQ> {
    OperatorResult create(REQ request);
    OperatorResult update(UUID id, REQ request);
    void deactiveById(UUID id);
    void deactiveByCode(String code);
}