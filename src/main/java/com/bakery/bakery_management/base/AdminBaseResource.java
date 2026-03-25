package com.bakery.bakery_management.base;


import com.bakery.bakery_management.domain.PageResult;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public abstract class AdminBaseResource<REQ, RES, E extends JpaEntity<UUID>> {

    protected abstract AdminOperationService<REQ, RES, E> getService();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorResult create(@RequestBody REQ request) {
        return getService().create(request);
    }

    @PutMapping("/{id}")
    public OperatorResult update(@PathVariable UUID id, @RequestBody REQ request) {
        return getService().update(id, request);
    }

    @GetMapping("/{id}")
    public RES getById(@PathVariable UUID id) {
        return getService().getById(id);
    }

    @GetMapping
    public Page<RES> getPage(@ParameterObject Pageable pageable) {
        return getService().getList(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        getService().delete(id);
    }
}