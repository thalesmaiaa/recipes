package com.recipes.core.domain;

import com.recipes.adapter.in.dto.response.RecipeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class Recipe {

    private Long id;
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> instructions;
    private Boolean vegetarian;
    private Integer servingSize;

    public Recipe(String title, String description, List<String> ingredients, List<String> instructions, boolean vegetarian, Integer servingSize) {
        this.title = title;
        this.servingSize = servingSize;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.vegetarian = Optional.of(vegetarian).orElse(Boolean.FALSE);
    }

    public RecipeResponse toResponse() {
        return new RecipeResponse(
                this.id,
                this.title,
                this.description,
                this.ingredients,
                this.instructions,
                this.vegetarian,
                this.servingSize
        );
    }

    public Boolean isVegetarian() {
        return this.vegetarian;
    }

}
