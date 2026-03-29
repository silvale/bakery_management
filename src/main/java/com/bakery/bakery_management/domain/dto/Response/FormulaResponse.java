package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class FormulaResponse extends BaseResponse {

    private ReferenceResponse product;

    private Integer version;

    private BigDecimal lossRate;

    private String componentType;

    private String description;

    private List<FormulaComponentResponse> components;
}
