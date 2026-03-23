package com.bakery.bakery_management.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_ingredient")
@Getter
@Setter
public class RecipeIngredient extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_product_id")
    private Long ingredientProductId;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit_id")
    private Long unitId;

}
