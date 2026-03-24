package com.bakery.bakery_management.domain.dto.Response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import com.bakery.bakery_management.domain.enums.ProductType;
import com.bakery.bakery_management.domain.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitResponse extends BaseResponse {

    private String code;
    private String name;

}
