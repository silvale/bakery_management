package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "recipe_ingredient")
@Getter
@Setter
public class RecipeIngredient extends JpaEntity<UUID> {

    @Column(name = "recipe_id")
    private UUID recipeId;

    @Column(name = "ingredient_product_id")
    private UUID ingredientProductId;

    @Column(name = "quantity")
    private Double quantity;

//    @Column(name = "unit_id")
//    private UUID unitId;

}
