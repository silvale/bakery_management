package com.bakery.bakery_management.base;


import com.bakery.bakery_management.domain.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public abstract class AdminBaseResource<REQ, RES, E extends JpaEntity<UUID>> {

    protected abstract AdminOperationService<REQ, RES, E> getService();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody REQ request) {
        getService().create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable UUID id, @RequestBody  REQ request) {
        getService().update(id, request);
    }

    @GetMapping("/{id}")
    public RES getById(@PathVariable UUID id) {
        return getService().getById(id);
    }

    @GetMapping
    public PageResult<RES> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return getService().getPage(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        getService().delete(id);
    }
}