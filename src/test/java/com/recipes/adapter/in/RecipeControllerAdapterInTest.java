package com.recipes.adapter.in;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;
import com.recipes.core.ports.in.CreateRecipePortIn;
import com.recipes.core.ports.in.DeleteRecipePortIn;
import com.recipes.core.ports.in.FindRecipePortIn;
import com.recipes.core.ports.in.UpdateRecipePortIn;
import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerAdapterInTest {

    private static final Long RECIPE_ID = 1L;

    @Mock
    private UpdateRecipePortIn updateRecipePortIn;

    @Mock
    private CreateRecipePortIn createRecipePortIn;

    @Mock
    private DeleteRecipePortIn deleteRecipePortIn;

    @Mock
    private FindRecipePortIn findRecipePortIn;
    
    @InjectMocks
    private RecipeControllerAdapterIn controller;

    @Test
    void shouldCreateRecipeAndReturnCreatedResponse() {
        var request = RecipeFactory.buildCreateRecipeRequest();

        when(createRecipePortIn.execute(any(Recipe.class))).thenReturn(RECIPE_ID);
        var response = controller.createRecipe(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/v1/recipes/" + RECIPE_ID));
        verify(createRecipePortIn).execute(any(Recipe.class));
    }

    @Test
    void shouldDeleteRecipe() {
        controller.deleteRecipe(RECIPE_ID);
        verify(deleteRecipePortIn).execute(RECIPE_ID);
    }

    @Test
    void shouldFindRecipeById() {
        var recipe = RecipeFactory.createRecipe();
        var recipeResponse = RecipeFactory.generateRecipeResponse(recipe);
        when(findRecipePortIn.execute(RECIPE_ID)).thenReturn(recipeResponse);

        var result = controller.findRecipeById(RECIPE_ID);

        assertThat(result).isEqualTo(recipeResponse);
        verify(findRecipePortIn).execute(RECIPE_ID);
    }

    @Test
    void shouldFindRecipesWithFilters() {
        var filters = new RecipeFiltersRequest(
                List.of("Spaghetti"),
                List.of("Meat"),
                "instructions",
                false,
                2
        );
        var pageable = mock(Pageable.class);
        var page = mock(Page.class);

        when(findRecipePortIn.execute(filters, pageable)).thenReturn(page);
        var result = controller.findRecipes(filters, pageable);
        assertThat(result).isEqualTo(page);

        verify(findRecipePortIn).execute(filters, pageable);
    }

    @Test
    void shouldUpdateRecipe() {
        var request = RecipeFactory.buildUpdateRecipeRequest();

        var response = mock(RecipeResponse.class);
        when(updateRecipePortIn.execute(any(Recipe.class))).thenReturn(response);

        var result = controller.updateRecipe(RECIPE_ID, request);

        assertThat(result).isEqualTo(response);
        verify(updateRecipePortIn).execute(any(Recipe.class));
    }
}
