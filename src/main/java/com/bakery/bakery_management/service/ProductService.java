package com.bakery.bakery_management.service;


import com.bakery.bakery_management.Utils.MappingUtils;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.dto.request.ProductPriceRequest;
import com.bakery.bakery_management.domain.dto.request.ProductRequest;
import com.bakery.bakery_management.domain.dto.response.FormulaComponentResponse;
import com.bakery.bakery_management.domain.dto.response.FormulaResponse;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.dto.response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Formula;
import com.bakery.bakery_management.domain.entity.FormulaComponent;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.mapper.*;
import com.bakery.bakery_management.repository.FormulaComponentRepository;
import com.bakery.bakery_management.repository.FormulaRepository;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import com.bakery.bakery_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService extends AdminOperationService<ProductRequest, ProductResponse, Product> {

    private final ProductRepository repository;
    private final ProductPriceRepository priceRepository;
    private final FormulaRepository formulaRepository;
    private final FormulaComponentRepository componentRepository;


    private final ProductMapper productMapper;
    private final FormulaMapper formulaMapper;
    private final ProductPriceMapper priceMapper;
    private final FormulaComponentMapper componentMapper;

    private final ProductPriceService priceService;
    private final UnitService unitService;


    @Override
    protected JpaRepository<Product, UUID> getRepository() {
        return this.repository;
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

        if (request.getFormula() != null) {

            if (request.getType() != ProductType.FINISHED) {
                throw new BusinessException("Only FINISHED product can have formula");
            }

            // validate ingredient
            request.getFormula().getComponents().forEach(c -> {
                Product ingredient = repository.findByCode(c.getProductCode())
                        .orElseThrow(() -> new BusinessException("Ingredient not found"));

                if (ingredient.getType() == ProductType.FINISHED) {
                    throw new BusinessException("Ingredient must be RAW or SEMI");
                }
            });
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

        if (request.getFormula() == null) return;

        Formula formula = formulaMapper.toEntity(request.getFormula());
        formula.setProductCode(entity.getCode());

        formula = formulaRepository.save(formula);
        Formula finalFormula = formula;
        List<FormulaComponent> components = request.getFormula()
                .getComponents()
                .stream()
                .map(req -> {
                    FormulaComponent c = componentMapper.toEntity(req);
                    c.setFormula(finalFormula);
                    return c;
                })
                .toList();

        componentRepository.saveAll(components);

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
        ReferenceResponse unitRef = unitService.getByCode(entity.getUnitCode());
        response.setUnit(unitRef);

        List<ProductPriceResponse> priceResponses = priceMapper.toResponse(allPrices).stream().toList();
        response.setPrices(priceResponses);

        priceResponses.stream()
                .filter(ProductPriceResponse::getIsDefault)
                .findFirst()
                .ifPresent(p -> {
                    response.setCurrentSalesPrice(p.getSalePrice());
                    response.setCurrentCostPrice(p.getCostPrice());
                });

        Optional<Formula> optional =
                formulaRepository.findTopByProductCodeOrderByVersionDesc(entity.getCode());

        if (optional.isEmpty()) return;

        Formula formula = optional.get();

        // map formula
        FormulaResponse formulaResponse = formulaMapper.toResponse(formula);

        List<FormulaComponent> components = formula.getComponents();
        List<FormulaComponentResponse> componentResponses =
                components.stream()
                        .map(componentMapper::toResponse)
                        .toList();

        formulaResponse.setComponents(componentResponses);
        response.setFormula(formulaResponse);
        MappingUtils.mapReference(
                List.of(formula),
                List.of(formulaResponse),
                Formula::getProductCode,
                this::getMapByCodes,
                FormulaResponse::setProduct
        );

        MappingUtils.mapReference(
                components,
                componentResponses,
                FormulaComponent::getProductCode,
                this::getMapByCodes,
                FormulaComponentResponse::setProduct
        );

        MappingUtils.mapReference(
                components,
                componentResponses,
                FormulaComponent::getUnitCode,
                unitService::getMapByCodes,
                FormulaComponentResponse::setUnit
        );
    }

    public Map<String, ReferenceResponse> getMapByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }
        List<Product> products = repository.findAllByCodeInAndStatus(codes, StatusCode.ACTIVE);
        return products.stream().collect(Collectors.toMap(
                Product::getCode,
                u -> new ReferenceResponse(u.getCode(), u.getName()),
                (existing, replacement) -> existing // Nếu trùng Code thì lấy cái đầu tiên, tránh crash
        ));
    }
}