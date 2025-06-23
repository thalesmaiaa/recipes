package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateRecipeRequest(@Size(max = 255) String title, @Size(max = 255) String description,
                                  List<String> ingredients, List<String> instructions, Boolean vegetarian,
                                  Integer servingSize) {

    public Recipe toDomain(Long id) {
        return new Recipe(id, title, description, ingredients, instructions, vegetarian, servingSize);
    }
}