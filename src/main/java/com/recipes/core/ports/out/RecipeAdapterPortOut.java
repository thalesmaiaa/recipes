package com.recipes.core.ports.out;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.core.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RecipeAdapterPortOut {

    Page<Recipe> findAll(RecipeFiltersRequest filtersRequest, Pageable pageable);

    Optional<Recipe> findRecipeById(Long id);

    void deleteRecipeById(Long id);

    Recipe saveRecipe(Recipe recipe);
}
