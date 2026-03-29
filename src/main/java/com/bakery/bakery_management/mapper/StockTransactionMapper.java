package com.bakery.bakery_management.mapper;


import com.bakery.bakery_management.domain.dto.request.StockTransactionRequest;
import com.bakery.bakery_management.domain.dto.response.StockTransactionResponse;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(config = CentralMapperConfig.class, componentModel = SPRING)
public interface StockTransactionMapper extends AdminBaseMapper<StockTransactionRequest, StockTransactionResponse, StockTransaction> {
}
