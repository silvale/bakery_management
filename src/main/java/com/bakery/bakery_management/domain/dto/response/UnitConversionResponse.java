package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UnitConversionResponse extends BaseResponse {

    private String fromUnitCode;

    private String toUnitCode;

    private BigDecimal ratio;

}
