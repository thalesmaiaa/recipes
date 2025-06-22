package com.recipes.adapter.in.dto.request;

import java.util.List;

public record RecipeFiltersRequest(List<String> ingredients, String instruction, Boolean vegetarian,
                                   Integer servingSize) {
}
