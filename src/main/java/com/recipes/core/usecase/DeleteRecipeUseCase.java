package com.recipes.core.usecase;

import com.recipes.core.ports.in.DeleteRecipePortIn;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteRecipeUseCase implements DeleteRecipePortIn {

    private final RecipeAdapterPortOut recipeAdapterPortOut;

    public void execute(Long id) {
        recipeAdapterPortOut.deleteRecipeById(id);
        log.info("DeleteRecipeUseCase.execute - Deleted recipe with id: [{}]", id);
    }
}
