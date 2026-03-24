package com.bakery.bakery_management.base;

import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public abstract class AdminOperationService<REQ, RES, E extends JpaEntity<UUID>>
        extends AdminEntitySupportService<REQ, RES, E>
        implements AdminCrudService<REQ>, QueryService<RES> {

    @Override
    protected abstract AdminBaseMapper<REQ, RES, E> getMapper();

    @Override
    @Transactional
    public OperatorResult create(REQ request) {
        try {
            E entity = getMapper().toEntity(request);
            E savedEntity = getRepository().save(entity);
            return OperatorResult.ok("Tạo thành công", savedEntity.getId());
        } catch (Exception e) {
            return OperatorResult.fail("Lỗi khi tạo: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<RES> getPage(Pageable pageable) {
        Page<E> page = getRepository().findAll(pageable);
        return PageResult.ofPage(page.map(getMapper()::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(UUID id) {
        return getRepository().findById(id)
                .map(getMapper()::toResponse)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    @Transactional
    public OperatorResult update(UUID id, REQ request) {
        E existingEntity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy"));

        getMapper().updateEntity(request, existingEntity);
        getRepository().save(existingEntity);

        return OperatorResult.ok("Cập nhật thành công", id);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        getRepository().deleteById(id);
    }
}
