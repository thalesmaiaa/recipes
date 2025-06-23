package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;

import java.util.List;

public record UpdateRecipeRequest(String title, String description, List<String> ingredients,
                                  List<String> instructions, Boolean vegetarian, Integer servingSize) {

    public Recipe toDomain(Long id) {
        return new Recipe(id, title, description, ingredients, instructions, vegetarian, servingSize);
    }
}