package com.recipes.core.usecase;

import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;
import com.recipes.core.exceptions.RecipeNotFoundException;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRecipeUseCaseTest {

    @Mock
    private RecipeAdapterPortOut recipeAdapterPortOut;

    @InjectMocks
    private UpdateRecipeUseCase updateRecipeUseCase;

    @Test
    void shouldUpdateRecipeAndReturnResponse() {
        var updateData = RecipeFactory.createRecipe();
        updateData.setTitle("Updated Spaghetti Bolognese");
        var existingRecipe = RecipeFactory.createRecipe();
        var updatedRecipe = mock(Recipe.class);
        var response = mock(RecipeResponse.class);

        when(recipeAdapterPortOut.findRecipeById(existingRecipe.getId())).thenReturn(Optional.of(existingRecipe));
        when(recipeAdapterPortOut.saveRecipe(existingRecipe)).thenReturn(updatedRecipe);
        when(updatedRecipe.toResponse()).thenReturn(response);

        var result = updateRecipeUseCase.execute(updateData);

        assertThat(result).isEqualTo(response);
        verify(recipeAdapterPortOut).findRecipeById(existingRecipe.getId());
        verify(recipeAdapterPortOut).saveRecipe(existingRecipe);
    }

    @Test
    void shouldThrowExceptionWhenRecipeNotFound() {
        var id = 2L;
        var updateData = mock(Recipe.class);
        when(updateData.getId()).thenReturn(id);
        when(recipeAdapterPortOut.findRecipeById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateRecipeUseCase.execute(updateData))
                .isInstanceOf(RecipeNotFoundException.class);
        verify(recipeAdapterPortOut, never()).saveRecipe(any());
    }
}
