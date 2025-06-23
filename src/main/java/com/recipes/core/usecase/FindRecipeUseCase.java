package com.recipes.core.usecase;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;
import com.recipes.core.exceptions.RecipeNotFoundException;
import com.recipes.core.ports.in.FindRecipePortIn;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class FindRecipeUseCase implements FindRecipePortIn {

    private final RecipeAdapterPortOut recipeAdapterPortOut;

    public RecipeResponse execute(Long id) {
        log.debug("FindRecipeUseCase.execute - recipeId: [{}]", id);
        var recipe = recipeAdapterPortOut.findRecipeById(id).orElseThrow(RecipeNotFoundException::new);
        return recipe.toResponse();
    }

    public Page<RecipeResponse> execute(RecipeFiltersRequest filtersRequest, Pageable pageable) {
        log.debug("FindRecipeUseCase.execute - filters: [{}], pageable: [{}]", filtersRequest, pageable);
        return recipeAdapterPortOut.findAll(filtersRequest, pageable).map(Recipe::toResponse);
    }
}
