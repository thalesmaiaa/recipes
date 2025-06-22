package com.recipes.core.ports.in;

import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;

public interface CreateRecipePortIn {

    RecipeResponse execute(Recipe recipe);
}
