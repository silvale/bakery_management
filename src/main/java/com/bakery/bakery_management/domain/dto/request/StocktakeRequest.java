package com.bakery.bakery_management.domain.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StocktakeRequest {

    LocalDate processDate;
    List<StocktakeItem> items;
}
