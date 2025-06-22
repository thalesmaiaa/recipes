package com.recipes.core.usecase;

import com.recipes.core.domain.Recipe;
import com.recipes.core.ports.in.CreateRecipePortIn;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRecipeUseCase implements CreateRecipePortIn {

    private final RecipeAdapterPortOut recipeAdapterPortOut;

    public Long execute(Recipe recipe) {
        var createdRecipe = recipeAdapterPortOut.saveRecipe(recipe);
        log.info("CreateRecipeUseCase.execute - Recipe [{}] created", createdRecipe.getId());
        return createdRecipe.getId();
    }
}
