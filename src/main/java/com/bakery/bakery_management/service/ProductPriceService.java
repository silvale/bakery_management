package com.bakery.bakery_management.service;

import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
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
    public void syncPrice(String productCode, String unitCode, BigDecimal cost, BigDecimal sale, ProductType type) {
        // 1. Tìm giá mặc định hiện tại cho sản phẩm và đơn vị này
        Optional<ProductPrice> currentPriceOpt = repository.findByProductCodeAndUnitCodeAndIsDefaultTrue(productCode, unitCode);

        // 2. Kiểm tra sự thay đổi (So sánh giá nhập hoặc giá bán tùy theo ProductType)
        boolean isChanged = currentPriceOpt.isEmpty();

        if (currentPriceOpt.isPresent()) {
            ProductPrice current = currentPriceOpt.get();
            // Nếu là hàng nguyên liệu (RAW/SEMI): check costPrice. Nếu là hàng bán (FINISHED): check salePrice
            boolean costChanged = (cost.compareTo(BigDecimal.ZERO) > 0 && cost.compareTo(current.getCostPrice()) != 0);
            boolean saleChanged = (sale.compareTo(BigDecimal.ZERO) > 0 && sale.compareTo(current.getSalePrice()) != 0);
            isChanged = costChanged || saleChanged;
        }

        if (isChanged) {
            // 3. Gỡ bỏ mặc định cũ (nếu có)
            currentPriceOpt.ifPresent(p -> {
                p.setIsDefault(false);
                repository.save(p);
            });

            // 4. Tạo bản ghi giá mới (Lịch sử giá)
            ProductPrice newPrice = new ProductPrice();
            newPrice.setCode("PRC-" + System.currentTimeMillis()); // Generate mã giá đơn giản
            newPrice.setProductCode(productCode);
            newPrice.setUnitCode(unitCode);
            newPrice.setCostPrice(cost);
            newPrice.setSalePrice(sale);
            newPrice.setAppliedDate(LocalDateTime.now());
            newPrice.setIsDefault(true); // Đặt làm giá mới nhất
            newPrice.setStatus(StatusCode.ACTIVE);

            repository.save(newPrice);
        }
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
}
