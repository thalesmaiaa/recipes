package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateRecipeRequest(@NotBlank @Size(max = 255) String title,
                                  @Size(max = 255) @NotBlank String description,
                                  @NotEmpty List<String> ingredients, @NotEmpty List<String> instructions,
                                  boolean vegetarian, @NotNull Integer servingSize) {

    public Recipe toDomain() {
        return new Recipe(title, description, ingredients, instructions, vegetarian, servingSize);
    }
}
