package com.recipes.adapter.in.dto.response;

import java.util.List;

public record RecipeResponse(Long id, String name, String description, List<String> ingredients,
                             List<String> instructions, boolean isVegetarian, Integer servingSize) {
}