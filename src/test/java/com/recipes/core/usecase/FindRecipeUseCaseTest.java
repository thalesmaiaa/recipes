package com.recipes.core.usecase;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.core.domain.Recipe;
import com.recipes.core.exceptions.RecipeNotFoundException;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRecipeUseCaseTest {

    @Mock
    private RecipeAdapterPortOut recipeAdapterPortOut;

    @InjectMocks
    private FindRecipeUseCase findRecipeUseCase;

    @Test
    void shouldReturnRecipeResponseWhenRecipeExists() {
        var id = 1L;
        var recipe = RecipeFactory.createVegetarianRecipe();
        var response = RecipeFactory.generateRecipeResponse(recipe);

        when(recipeAdapterPortOut.findRecipeById(id)).thenReturn(Optional.of(recipe));

        var result = findRecipeUseCase.execute(id);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowExceptionWhenRecipeNotFound() {
        var id = 1L;
        when(recipeAdapterPortOut.findRecipeById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> findRecipeUseCase.execute(id)).isInstanceOf(RecipeNotFoundException.class);
    }

    @Test
    void shouldReturnPageOfRecipeResponses() {
        var filtersRequest = mock(RecipeFiltersRequest.class);
        var pageable = mock(Pageable.class);
        Page<?> page = mock(Page.class);

        when(recipeAdapterPortOut.findAll(filtersRequest, pageable)).thenReturn((Page<Recipe>) page);
        when(page.map(any())).thenReturn((Page<Object>) page);

        var result = findRecipeUseCase.execute(filtersRequest, pageable);

        assertThat(result).isEqualTo(page);
        verify(recipeAdapterPortOut).findAll(filtersRequest, pageable);
    }
}
