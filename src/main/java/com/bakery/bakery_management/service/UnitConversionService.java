package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.UnitConversionRequest;
import com.bakery.bakery_management.domain.dto.response.UnitConversionResponse;
import com.bakery.bakery_management.domain.entity.UnitConversion;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.UnitConversionMapper;
import com.bakery.bakery_management.repository.UnitConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitConversionService extends AdminOperationService<UnitConversionRequest, UnitConversionResponse, UnitConversion> {

    private final UnitConversionRepository repository;
    private final UnitConversionMapper mapper;

    public BigDecimal convert(BigDecimal quantity,
                              String fromUnit,
                              String toUnit) {

        if (quantity == null) {
            return BigDecimal.ZERO;
        }

        if (fromUnit == null || toUnit == null) {
            throw new IllegalArgumentException("Unit must not be null");
        }

        // 1. same unit → return luôn
        if (fromUnit.equals(toUnit)) {
            return quantity;
        }

        // 2. tìm conversion direct
        Optional<UnitConversion> directOpt =
                repository.findByFromUnitCodeAndToUnitCode(fromUnit, toUnit);

        if (directOpt.isPresent()) {
            BigDecimal rate = directOpt.get().getRatio();
            return quantity.multiply(rate);
        }

        // 3. tìm conversion reverse
        Optional<UnitConversion> reverseOpt =
                repository.findByFromUnitCodeAndToUnitCode(toUnit, fromUnit);

        if (reverseOpt.isPresent()) {
            BigDecimal rate = reverseOpt.get().getRatio();
            return quantity.divide(rate, 6, RoundingMode.HALF_UP);
        }

        // 4. không tìm thấy
        throw new BusinessException(
                String.format("No unit conversion found from %s to %s", fromUnit, toUnit)
        );
    }

    @Override
    protected JpaRepository<UnitConversion, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<UnitConversionRequest, UnitConversionResponse, UnitConversion> getMapper() {
        return this.mapper;
    }
}