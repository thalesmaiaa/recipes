package com.recipes.core.usecase;

import com.recipes.core.ports.out.RecipeAdapterPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeUseCaseTest {

    @Mock
    private RecipeAdapterPortOut recipeAdapterPortOut;

    @InjectMocks
    private DeleteRecipeUseCase deleteRecipeUseCase;

    @Test
    void shouldDeleteRecipeById() {
        var id = 1L;
        deleteRecipeUseCase.execute(id);
        verify(recipeAdapterPortOut).deleteRecipeById(id);
    }
}
