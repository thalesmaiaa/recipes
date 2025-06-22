package com.recipes.core.ports.in;

import com.recipes.core.domain.Recipe;

public interface CreateRecipePortIn {

    Long execute(Recipe recipe);
}
