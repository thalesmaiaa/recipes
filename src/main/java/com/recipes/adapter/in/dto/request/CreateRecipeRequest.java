package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateRecipeRequest(@NotBlank String name, @NotBlank String description,
                                  @NotEmpty List<String> ingredients, @NotEmpty List<String> instructions,
                                  boolean isVegetarian, @NotNull Integer servingSize) {

    public Recipe toDomain() {
        return new Recipe(name, description, ingredients, instructions, isVegetarian, servingSize);
    }
}
