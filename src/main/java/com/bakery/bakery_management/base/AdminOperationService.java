package com.bakery.bakery_management.base;

import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public abstract class AdminOperationService<REQ, RES, E extends JpaEntity<UUID>>
        extends AdminEntitySupportService<REQ, RES, E>
        implements AdminCrudService<REQ>, QueryService<RES> {

    @Override
    protected abstract AdminBaseMapper<REQ, RES, E> getMapper();

    // --- CÁC HOOKS ĐỂ HANDLE LOGIC ---

    // Trước và sau khi TẠO MỚI
    protected void beforeCreate(REQ request, E entity) {
    }

    protected void afterCreate(REQ request, E entity) {
    }

    // Trước và sau khi CẬP NHẬT
    protected void beforeUpdate(REQ request, E entity, E oldEntity) {
    }

    protected void afterUpdate(REQ request, E entity) {
    }

    // Trước và sau khi XÓA
    protected void beforeDelete(UUID id, E entity) {
    }

    protected void afterDelete(UUID id) {
    }

    protected void afterGetListProduct(List<E> entities, List<RES> responses) {
    }

    ;

    protected void afterDetail(E entity, RES response) {
    }

    @Override
    @Transactional
    public OperatorResult create(REQ request) {
        try {
            E entity = getMapper().toEntity(request);
            beforeCreate(request, entity);
            E savedEntity = getRepository().save(entity);
            afterCreate(request, savedEntity);
            return OperatorResult.ok("Tạo thành công", savedEntity.getId());
        } catch (Exception e) {
            return OperatorResult.fail("Lỗi khi tạo: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OperatorResult update(UUID id, REQ request) {
        E existingEntity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy"));

        E entity = getMapper().toEntity(request);
        entity.setId(id);
        beforeUpdate(request, entity, existingEntity);
        getMapper().updateEntity(request, existingEntity);
        E saved = getRepository().save(existingEntity);
        afterUpdate(request, entity);

        return OperatorResult.ok("Cập nhật thành công", id);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        E entity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Không tìm thấy dữ liệu để xóa"));

        beforeDelete(id, entity);

        entity.setStatus(StatusCode.DELETED);

        getRepository().save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RES> getList(Pageable pageable) {
        // Bước A: Lấy dữ liệu phân trang từ DB
        Page<E> entityPage = getRepository().findAll(pageable);

        // Bước B: Chuyển đổi sang List Response (Dùng toResponse của Hải)
        List<RES> responseList = entityPage.getContent().stream()
                .map(getMapper()::toResponse)
                .collect(Collectors.toList());

        // Bước C: GỌI HOOK TẠI ĐÂY - Đây là nơi Hải xử lý Batch Load (như load giá)
        afterGetListProduct(entityPage.getContent(), responseList);

        // Bước D: Trả về Page mới chứa dữ liệu đã được "làm giàu"
        return new PageImpl<>(responseList, pageable, entityPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(UUID id) {
        E entity = getRepository().findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Không tìm thấy dữ liệu với ID: " + id));

        RES response = getMapper().toResponse(entity);

        afterDetail(entity, response);

        return response;
    }
}
