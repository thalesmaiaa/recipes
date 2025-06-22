package com.recipes.adapter.in.dto.request;

import com.recipes.core.domain.Recipe;

import java.util.List;

public record UpdateRecipeRequest(String name, String description, List<String> ingredients,
                                  List<String> instructions, Boolean isVegetarian, Integer servingSize) {

    public Recipe toDomain(Long id) {
        return new Recipe(id, name, description, ingredients, instructions, isVegetarian, servingSize);
    }
}