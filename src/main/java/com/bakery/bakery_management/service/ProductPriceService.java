package com.bakery.bakery_management.service;

import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductPriceMapper;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductPriceService extends AdminOperationService<ProductPriceRequest, ProductPriceResponse, ProductPrice> {

    private final ProductPriceMapper mapper;

    private final ProductPriceRepository repository;

    public ProductPriceService(ProductPriceMapper mapper, ProductPriceRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    protected JpaRepository<ProductPrice, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<ProductPriceRequest, ProductPriceResponse, ProductPrice> getMapper() {
        return this.mapper;
    }

    @Transactional
    public void syncPrice(String code,
                          String productCode,
                          String unitCode,
                          BigDecimal cost,
                          BigDecimal sale,
                          boolean isDefault,
                          ProductType type) {

        Optional<ProductPrice> currentPriceOpt =
                repository.findByCodeAndProductCodeAndUnitCode(code, productCode, unitCode);

        boolean isChanged = currentPriceOpt.isEmpty();

        if (currentPriceOpt.isPresent()) {
            ProductPrice current = currentPriceOpt.get();
            boolean costChanged = isDifferent(cost, current.getCostPrice());
            boolean saleChanged = isDifferent(sale, current.getSalePrice());

            if (type == ProductType.RAW || type == ProductType.SEMI) {
                isChanged = costChanged;
            } else if (type == ProductType.FINISHED) {
                isChanged = costChanged || saleChanged;
            }

            if (isChanged) {
                current.setCostPrice(cost);
                current.setSalePrice(sale);
                current.setIsDefault(isDefault);
                repository.save(current);
                return;
            }
        }

        if (!isChanged) {
            return;
        }

        ProductPrice newPrice = new ProductPrice();
        newPrice.setCode(code);
        newPrice.setProductCode(productCode);
        newPrice.setUnitCode(unitCode);
        newPrice.setCostPrice(cost);
        newPrice.setSalePrice(sale);
        newPrice.setAppliedDate(LocalDateTime.now());
        newPrice.setIsDefault(isDefault);
        newPrice.setStatus(StatusCode.ACTIVE);

        repository.save(newPrice);
    }

    public Map<String, ProductPriceResponse> getDefaultPriceResponsesByCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1. Lấy toàn bộ giá mặc định từ DB (1 Query)
        List<ProductPrice> defaultPrices = repository.findAllDefaultPrices(codes);

        // 2. Chuyển thành Map: Key là ProductCode, Value là SalePrice
        return defaultPrices.stream()
                .collect(Collectors.toMap(
                        ProductPrice::getProductCode,
                        mapper::toResponse, // Map sang Response luôn ở đây
                        (existing, replacement) -> existing
                ));
    }

    private boolean isDifferent(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return false;
        if (a == null || b == null) return true;
        return a.compareTo(b) != 0;
    }

    public List<ProductPrice> getListPriceByProduct(String productCode) {
        return repository.findByProductCode(productCode);
    }

    @Override
    public void deactiveByCode(String code) {
        ProductPrice price = repository.findByCode(code).orElse(null);
        if (price != null) {
            price.setIsDefault(false);
            price.setStatus(StatusCode.INACTIVE);
            repository.save(price);
        }
    }
}
