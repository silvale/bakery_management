package com.bakery.bakery_management.service.Impl;

import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.service.AdminBaseService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Transactional
public abstract class AdminBaseServiceImpl<E, Req, Res, ID>
        implements AdminBaseService<Req, Res, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    protected abstract AdminBaseMapper<Req, Res, E> getMapper();

    protected abstract ID getId(E entity);

    // ===== CREATE =====
    @Override
    public ID create(Req request) {

        validateCreate(request);

        E entity = getMapper().toEntity(request);

        beforeCreate(entity, request);

        getRepository().save(entity);

        afterCreate(entity);

        return getId(entity);
    }

    // ===== DETAIL =====
    @Override
    @Transactional(readOnly = true)
    public Res getDetail(ID id) {

        E entity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND"));

        return getMapper().toResponse(entity);
    }

    // ===== LIST =====
    @Override
    @Transactional(readOnly = true)
    public List<Res> getList() {
        List<E> list = getRepository().findAll().stream().toList();
        return (List<Res>) getMapper().toResponse(list);
    }

    // ===== UPDATE (PATCH style) =====
    @Override
    public void update(ID id, Req request) {

        E entity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND"));

        validateUpdate(request, entity);

        getMapper().patch(request, entity); // 🔥 dùng patch

        beforeUpdate(entity, request);

        getRepository().save(entity);

        afterUpdate(entity);
    }

    // ===== DELETE =====
    @Override
    public void delete(ID id) {

        E entity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND"));

        beforeDelete(entity);

        getRepository().delete(entity);

        afterDelete(entity);
    }

    // ===== HOOK METHODS =====

    protected void validateCreate(Req request) {}

    protected void validateUpdate(Req request, E entity) {}

    protected void beforeCreate(E entity, Req request) {}

    protected void afterCreate(E entity) {}

    protected void beforeUpdate(E entity, Req request) {}

    protected void afterUpdate(E entity) {}

    protected void beforeDelete(E entity) {}

    protected void afterDelete(E entity) {}
}