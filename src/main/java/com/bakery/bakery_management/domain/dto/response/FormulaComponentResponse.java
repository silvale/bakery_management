package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class FormulaComponentResponse extends BaseResponse {

    private UUID formulaId;

    private ReferenceResponse product; // enrich

    private BigDecimal quantity;

    private ReferenceResponse unit; // enrich

    private String note;
}
