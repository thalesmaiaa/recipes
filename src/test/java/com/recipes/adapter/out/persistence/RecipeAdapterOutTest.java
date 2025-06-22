package com.recipes.adapter.out.persistence;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.core.domain.Recipe;
import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeAdapterOutTest {

    private static final Long RECIPE_ID = 1L;

    @Mock
    private RecipeRepository recipeRepository;
    
    @InjectMocks
    private RecipeAdapterOut adapter;

    @Test
    void shouldFindAllRecipesWithFilters() {
        var filters = mock(RecipeFiltersRequest.class);
        var pageable = mock(Pageable.class);
        var entityPage = mock(Page.class);

        when(recipeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(entityPage);
        when(entityPage.map(any())).thenAnswer(invocation -> mock(Page.class));

        var result = adapter.findAll(filters, pageable);

        assertThat(result).isNotNull();
        verify(recipeRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldFindRecipeById() {
        var entity = mock(RecipeEntity.class);
        var recipe = mock(Recipe.class);

        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(recipe);

        var result = adapter.findRecipeById(RECIPE_ID);

        assertThat(result).contains(recipe);
        verify(recipeRepository).findById(RECIPE_ID);
    }

    @Test
    void shouldReturnEmptyWhenRecipeNotFound() {
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.empty());
        var result = adapter.findRecipeById(RECIPE_ID);

        assertThat(result).isEmpty();
        verify(recipeRepository).findById(RECIPE_ID);
    }

    @Test
    void shouldDeleteRecipeById() {
        adapter.deleteRecipeById(RECIPE_ID);
        verify(recipeRepository).deleteById(RECIPE_ID);
    }

    @Test
    void shouldSaveRecipe() {
        var recipe = RecipeFactory.createRecipe();
        var savedEntity = mock(RecipeEntity.class);
        var savedRecipe = RecipeFactory.createRecipe();

        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(savedEntity);
        when(savedEntity.toDomain()).thenReturn(savedRecipe);

        var result = adapter.saveRecipe(recipe);

        assertThat(result).isEqualTo(savedRecipe);
    }
}
