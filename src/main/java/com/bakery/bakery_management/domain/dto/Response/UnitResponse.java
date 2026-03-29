package com.bakery.bakery_management.domain.dto.response;

import com.bakery.bakery_management.domain.dto.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitResponse extends BaseResponse {

    private String code;
    private String name;

}
