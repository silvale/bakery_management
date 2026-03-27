package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductMapper;
import com.bakery.bakery_management.mapper.ProductPriceMapper;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import com.bakery.bakery_management.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService extends AdminOperationService<ProductRequest, ProductResponse, Product> {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductPriceService priceService;
    private final ProductPriceRepository priceRepository;
    private final ProductPriceMapper priceMapper;

    private final UnitService unitService;

    // Constructor Injection
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductPriceService priceService, ProductPriceRepository productPriceRepository, ProductPriceMapper priceMapper, UnitService unitService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.priceService = priceService;
        this.priceRepository = productPriceRepository;
        this.priceMapper = priceMapper;
        this.unitService = unitService;
    }

    @Override
    protected JpaRepository<Product, UUID> getRepository() {
        return this.productRepository;
    }

    @Override
    protected AdminBaseMapper<ProductRequest, ProductResponse, Product> getMapper() {
        return this.productMapper;
    }


    @Override
    protected void beforeCreate(ProductRequest request, Product entity) {
        String unitCode = request.getUnitCode();
        boolean checkUnitExist = unitService.checkExistByCode(unitCode);
        if (!checkUnitExist) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Không tìm thấy Unit code : " + unitCode);
        }
        ExpiryInputType expiryType = request.getExpiryType();
        if (ExpiryInputType.TODAY.equals(expiryType)) {
            Integer expiryNumber = request.getDefaultExpiryDays();
            if (expiryNumber == null || expiryNumber <= 0) {
                throw new BusinessException(ErrorCode.INVALID_INPUT, "Ngày hết hạn không hợp lệ " + expiryNumber);
            }
        } else if (ExpiryInputType.NONE.equals(expiryType)) {
            request.setDefaultExpiryDays(null);
        }
    }

    @Override
    protected void afterCreate(ProductRequest request, Product entity) {
        List<ProductPriceRequest> prices = request.getPrices();
        if (prices != null && !prices.isEmpty()) {
            for (ProductPriceRequest price : prices) {
                priceService.syncPrice(request.getCode(), request.getUnitCode(), price.getCostPrice(), price.getSalePrice(), request.getType());
            }
        }

    }

    @Override
    protected void afterUpdate(ProductRequest request, Product entity) {
        List<ProductPriceRequest> prices = request.getPrices();
        for (ProductPriceRequest price : prices) {
            priceService.syncPrice(request.getCode(), request.getUnitCode(), price.getCostPrice(), price.getSalePrice(), request.getType());
        }
    }

    @Override
    protected void afterGetList(List<Product> entities, List<ProductResponse> responses) {
        List<String> codes = entities.stream()
                .map(Product::getCode)
                .filter(Objects::nonNull)
                .toList();

        List<String> unitCodes = entities.stream()
                .map(Product::getUnitCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, ProductPriceResponse> priceMap = priceService.getDefaultPriceResponsesByCodes(codes);
        Map<String, ReferenceResponse> unitMap = unitService.getMapByCodes(unitCodes);

        for (int i = 0; i < entities.size(); i++) {
            Product entity = entities.get(i);
            ProductResponse res = responses.get(i);

            if (entity.getUnitCode() != null && unitMap != null) {
                res.setUnit(unitMap.get(entity.getUnitCode()));
            }

            ProductPriceResponse priceRes = priceMap.get(entity.getCode());
            res.setCurrentSalesPrice(priceRes != null ? priceRes.getSalePrice() : BigDecimal.ZERO);
            res.setCurrentCostPrice(priceRes != null ? priceRes.getCostPrice() : BigDecimal.ZERO);

            res.setPrices(null);
        }
    }

    @Override
    protected void afterDetail(Product entity, ProductResponse response) {
        List<ProductPrice> allPrices = priceRepository
                .findByProductCodeAndStatusOrderByAppliedDateDesc(entity.getCode(), StatusCode.ACTIVE);
        List<ProductPriceResponse> priceResponses = priceMapper.toResponse(allPrices).stream().toList();
        ReferenceResponse unitRef = unitService.getByCode(entity.getUnitCode());
        response.setUnit(unitRef);
        response.setPrices(priceResponses);

        priceResponses.stream()
                .filter(ProductPriceResponse::getIsDefault)
                .findFirst()
                .ifPresent(p -> {
                    response.setCurrentSalesPrice(p.getSalePrice());
                    response.setCurrentCostPrice(p.getCostPrice());
                });
    }
}