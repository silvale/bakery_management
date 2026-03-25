package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.Request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductPriceResponse;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductMapper;
import com.bakery.bakery_management.mapper.ProductPriceMapper;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import com.bakery.bakery_management.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService extends AdminOperationService<ProductRequest, ProductResponse, Product> {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductPriceService priceService;
    private final ProductPriceRepository priceRepository;
    private final ProductPriceMapper priceMapper;

    // Constructor Injection
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductPriceService priceService, ProductPriceRepository productPriceRepository, ProductPriceMapper priceMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.priceService = priceService;
        this.priceRepository = productPriceRepository;
        this.priceMapper = priceMapper;
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
    protected void afterCreate(ProductRequest request, Product entity) {
        List<ProductPriceRequest> prices = request.getPrices();
        for (ProductPriceRequest price : prices) {
            priceService.syncPrice(request.getCode(), request.getUnitCode(), price.getCostPrice(), price.getSalePrice(), request.getType());
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
    protected void afterGetListProduct(List<Product> entities, List<ProductResponse> responses) {
        List<String> codes = entities.stream()
                .map(Product::getCode)
                .filter(Objects::nonNull)
                .toList();

        Map<String, ProductPriceResponse> priceMap = priceService.getDefaultPriceResponsesByCodes(codes);

        responses.forEach(res -> {
            ProductPriceResponse priceRes = priceMap.get(res.getCode());
            if (priceRes != null) {
                res.setCurrentSalesPrice(priceRes.getSalePrice());
                res.setCurrentCostPrice(priceRes.getCostPrice());
            } else {
                res.setCurrentSalesPrice(BigDecimal.ZERO);
                res.setCurrentCostPrice(BigDecimal.ZERO);
            }
            res.setPrices(null);
        });
    }

    @Override
    protected void afterDetail(Product entity, ProductResponse response) {
        // 1. Load toàn bộ lịch sử giá (Active) của sản phẩm này
        List<ProductPrice> allPrices = priceRepository
                .findByProductCodeAndStatusOrderByAppliedDateDesc(entity.getCode(), StatusCode.ACTIVE);

        // 2. Map sang List Response và đổ vào trường 'prices'
        List<ProductPriceResponse> priceResponses = priceMapper.toResponse(allPrices).stream().toList();;
        response.setPrices(priceResponses);

        // 3. Tìm cái nào là mặc định để hiển thị lên field giá "hiện tại" ở Header trang detail
        priceResponses.stream()
                .filter(ProductPriceResponse::isDefault)
                .findFirst()
                .ifPresent(p -> {
                    response.setCurrentSalesPrice(p.getSalePrice());
                    response.setCurrentCostPrice(p.getCostPrice());
                });
    }
}