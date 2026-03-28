package com.bakery.bakery_management.mapper;

import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ProductLookupContext {
    private final Map<String, ReferenceResponse> unitMap;

    private final Map<String, ReferenceResponse> productMap;

    // Hàm helper để Mapper gọi gọn hơn
    public ReferenceResponse getProduct(String code) {
        return productMap != null ? productMap.get(code) : null;
    }

    public ReferenceResponse getUnit(String code) {
        return unitMap != null ? unitMap.get(code) : null;
    }
}
