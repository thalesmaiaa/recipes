package com.recipes.adapter.in.dto.response;

import java.util.List;

public record RecipeResponse(Long id, String title, String description, List<String> ingredients,
                             List<String> instructions, Boolean vegetarian, Integer servingSize) {
}