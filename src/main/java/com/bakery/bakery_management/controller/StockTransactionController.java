package com.bakery.bakery_management.controller;

import com.bakery.bakery_management.base.AdminBaseResource;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.request.StockTransactionRequest;
import com.bakery.bakery_management.domain.dto.response.StockTransactionResponse;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.service.StockTransactionService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stock-transactions")
@RequiredArgsConstructor
public class StockTransactionController extends AdminBaseResource<StockTransactionRequest, StockTransactionResponse, StockTransaction> {

    private final StockTransactionService service;


    @GetMapping("/by-product/{warehouse}")
    public PageResult<StockTransactionResponse> getStockTransactionByProductCode(@PathVariable String warehouse,
                                                                                 @RequestParam("productCode") String productCode,
                                                                                 @ParameterObject Pageable pageable) {
        return service.getInventoryDetailByProductCode(pageable, warehouse, productCode);
    }

    @GetMapping("/by-reference/{warehouse}")
    public PageResult<StockTransactionResponse> getStockTransactionByReferenceId(@PathVariable String warehouse,
                                                                                 @RequestParam("referenceId") String referenceId,
                                                                                 @ParameterObject Pageable pageable) {
        return service.getInventoryDetailByReferenceId(pageable, warehouse, referenceId);
    }

    @GetMapping("/by-date/{warehouse}")
    public PageResult<StockTransactionResponse> getStockTransactionByDate(@PathVariable String warehouse,
                                                                          @RequestParam("day") LocalDate date,
                                                                          @ParameterObject Pageable pageable) {
        return service.getInventoryDetailByDate(pageable, date);
    }

    @Override
    protected AdminOperationService<StockTransactionRequest, StockTransactionResponse, StockTransaction> getService() {
        return service;
    }
}