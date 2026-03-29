package com.bakery.bakery_management.service;


import com.bakery.bakery_management.Utils.MappingUtils;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.request.StockTransactionRequest;
import com.bakery.bakery_management.domain.dto.response.StockTransactionResponse;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.StockTransactionMapper;
import com.bakery.bakery_management.repository.StockTransactionRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class StockTransactionService extends AdminOperationService<StockTransactionRequest, StockTransactionResponse, StockTransaction> {

    private final StockTransactionRepository repository;
    private final StockTransactionMapper mapper;
    private final ProductService productService;
    private final UnitService unitService;


    // Constructor Injection
    public StockTransactionService(StockTransactionRepository repository, StockTransactionMapper mapper, ProductService productService, UnitService unitService) {
        this.repository = repository;
        this.mapper = mapper;
        this.productService = productService;
        this.unitService = unitService;
    }

    @Override
    protected JpaRepository<StockTransaction, UUID> getRepository() {
        return this.repository;
    }

    @Override
    protected AdminBaseMapper<StockTransactionRequest, StockTransactionResponse, StockTransaction> getMapper() {
        return this.mapper;
    }

    public PageResult<StockTransactionResponse> getInventoryDetailByProductCode(
            Pageable pageable,
            String warehouseType,
            String productCode
    ) {
        List<StockTransaction> transactions =
                repository.findByWarehouseTypeAndProductCode(
                        WarehouseType.valueOf(warehouseType),
                        productCode
                );

        return buildInventoryDetail(pageable, transactions);
    }

    public PageResult<StockTransactionResponse> getInventoryDetailByReferenceId(
            Pageable pageable,
            String warehouseType,
            String referenceId
    ) {
        List<StockTransaction> transactions =
                repository.findByWarehouseTypeAndReferenceId(
                        WarehouseType.valueOf(warehouseType),
                        referenceId
                );

        return buildInventoryDetail(pageable, transactions);
    }

    public PageResult<StockTransactionResponse> getInventoryDetailByDate(
            Pageable pageable,
            LocalDate date
    ) {
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

        Instant start = date.atStartOfDay(zone).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(zone).toInstant();

        List<StockTransaction> transactions =
                repository.findByCreatedAtBetween(start, end);

        return buildInventoryDetail(pageable, transactions);
    }

    private PageResult<StockTransactionResponse> buildInventoryDetail(
            Pageable pageable,
            List<StockTransaction> transactions
    ) {
        List<StockTransactionResponse> responses =
                transactions.stream()
                        .map(getMapper()::toResponse)
                        .toList();

        // Mapping data
        enrich(transactions, responses);

        return PageResult.ofPage(new PageImpl<>(responses, pageable, responses.size()));
    }

    private void enrich(
            List<StockTransaction> entities,
            List<StockTransactionResponse> responses
    ) {
        MappingUtils.mapReference(
                entities,
                responses,
                StockTransaction::getUnitCode,
                unitService::getMapByCodes,
                StockTransactionResponse::setUnit
        );

        MappingUtils.mapReference(
                entities,
                responses,
                StockTransaction::getProductCode,
                productService::getMapByCodes,
                StockTransactionResponse::setProduct
        );
    }
}