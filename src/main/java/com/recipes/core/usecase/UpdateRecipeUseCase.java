package com.recipes.core.usecase;

import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;
import com.recipes.core.exceptions.RecipeNotFoundException;
import com.recipes.core.ports.in.UpdateRecipePortIn;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateRecipeUseCase implements UpdateRecipePortIn {

    private final RecipeAdapterPortOut recipeAdapterPortOut;

    public RecipeResponse execute(Recipe updateData) {
        var recipeId = updateData.getId();
        log.info("UpdateRecipeUseCase.execute - Start - recipeId: [{}]", recipeId);
        var recipe = recipeAdapterPortOut.findRecipeById(recipeId).orElseThrow(RecipeNotFoundException::new);
        patchRecipe(recipe, updateData);
        var updatedRecipe = recipeAdapterPortOut.saveRecipe(recipe);
        log.info("UpdateRecipeUseCase.execute - End - recipeId: [{}]", recipeId);
        return updatedRecipe.toResponse();
    }

    public void patchRecipe(Recipe recipe, Recipe recipeData) {
        recipe.setTitle(Objects.requireNonNullElse(recipeData.getTitle(), recipe.getTitle()));
        recipe.setDescription(Objects.requireNonNullElse(recipeData.getDescription(), recipe.getDescription()));
        recipe.setIngredients(Objects.requireNonNullElse(recipeData.getIngredients(), recipe.getIngredients()));
        recipe.setInstructions(Objects.requireNonNullElse(recipeData.getInstructions(), recipe.getInstructions()));
        recipe.setVegetarian(Objects.requireNonNullElse(recipeData.isVegetarian(), recipe.isVegetarian()));
        recipe.setServingSize(Objects.requireNonNullElse(recipeData.getServingSize(), recipe.getServingSize()));
    }
}
