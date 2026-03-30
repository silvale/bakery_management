package com.bakery.bakery_management.service;

import com.bakery.bakery_management.domain.entity.Formula;
import com.bakery.bakery_management.domain.entity.FormulaComponent;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FormulaCostService {

    private final ProductPriceRepository repository;
    private final ProductPriceService productPriceService;
    private final UnitConversionService unitConversionService;

    public void createCost(String productCode, BigDecimal cost) {

        // disable old active
        repository.deactivateByProductCode(productCode);
        ProductPrice price = new ProductPrice();
        price.setProductCode(productCode);
        price.setCostPrice(cost);
        price.setIsDefault(true);
        price.setAppliedDate(LocalDateTime.now());
        repository.save(price);
    }

    public BigDecimal calculateCost(Formula formula) {
        return formula.getComponents().stream()
                .map(this::calculateComponentCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateComponentCost(FormulaComponent c) {
        return BigDecimal.ZERO;
//        BigDecimal unitCost = productPriceService.getLatestCost(c.getProductCode());
//        String priceUnit = productPriceService.getLatestUnit(c.getProductCode());
//
//        BigDecimal quantity = unitConversionService.convert(
//                c.getQuantity(),
//                c.getUnitCode(),
//                priceUnit
//        );
//
//        return unitCost.multiply(quantity);

    }


}
