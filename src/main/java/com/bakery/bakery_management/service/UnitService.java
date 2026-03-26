package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.dto.Request.UnitRequest;
import com.bakery.bakery_management.domain.dto.Response.UnitResponse;
import com.bakery.bakery_management.domain.entity.Unit;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.UnitMapper;
import com.bakery.bakery_management.repository.UnitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UnitService extends AdminOperationService<UnitRequest, UnitResponse, Unit> {

    private final UnitRepository repository;
    private final UnitMapper mapper;

    // Constructor Injection
    public UnitService(UnitRepository repository, UnitMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Unit, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<UnitRequest, UnitResponse, Unit> getMapper() {
        return this.mapper;
    }


    public boolean checkExistByCode(String code) {
        return repository.existsByCode(code);
    }

    public Map<String, ReferenceResponse> getMapByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }

        // 1. Tìm tất cả Unit theo danh sách Code truyền vào
        List<Unit> units = repository.findAllByCodeInAndStatus(codes, StatusCode.ACTIVE);

        // 2. Chuyển đổi List thành Map <Code, ReferenceResponse>
        return units.stream().collect(Collectors.toMap(
                Unit::getCode,
                u -> new ReferenceResponse(u.getCode(), u.getName()),
                (existing, replacement) -> existing // Nếu trùng Code thì lấy cái đầu tiên, tránh crash
        ));
    }

}