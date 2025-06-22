package com.recipes.adapter.in.dto.request;

import java.util.List;

public record RecipeFiltersRequest(List<String> includedIngredients, List<String> excludedIngredients,
                                   String instruction,
                                   Boolean vegetarian, Integer servingSize) {
}
