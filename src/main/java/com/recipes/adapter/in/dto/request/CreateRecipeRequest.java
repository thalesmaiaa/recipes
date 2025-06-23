package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateRecipeRequest(@NotBlank @Size(max = 255) String title,
                                  @Size(max = 255) @NotBlank String description,
                                  @NotEmpty List<String> ingredients, @NotEmpty List<String> instructions,
                                  boolean vegetarian, @NotNull @Min(1) Integer servingSize) {

    public Recipe toDomain() {
        return new Recipe(title, description, ingredients, instructions, vegetarian, servingSize);
    }
}
