package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.dto.request.SupplierRequest;
import com.bakery.bakery_management.domain.dto.response.SupplierResponse;
import com.bakery.bakery_management.domain.entity.Supplier;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.SupplierMapper;
import com.bakery.bakery_management.repository.SupplierRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SupplierService extends AdminOperationService<SupplierRequest, SupplierResponse, Supplier> {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;

    // Constructor Injection
    public SupplierService(SupplierRepository repository, SupplierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Supplier, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<SupplierRequest, SupplierResponse, Supplier> getMapper() {
        return this.mapper;
    }

    public ReferenceResponse getByCode(String code) {
        Supplier supplier = repository.findByCode(code).orElse(null);
        ReferenceResponse supplierRes = new ReferenceResponse();
        if (supplier != null) {
            supplierRes.setCode(supplier.getCode());
            supplierRes.setName(supplier.getName());
        }
        return supplierRes;
    }

    public Map<String, ReferenceResponse> getMapByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }

        List<Supplier> Suppliers = repository.findAllByCodeIn(codes);

        return Suppliers.stream().collect(Collectors.toMap(
                Supplier::getCode,
                u -> new ReferenceResponse(u.getCode(), u.getName()),
                (existing, replacement) -> existing // Nếu trùng Code thì lấy cái đầu tiên, tránh crash
        ));
    }

}