package com.recipes.core.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {

    @Test
    void shouldCreateRecipeWithAllArgsConstructor() {
        var recipe = new Recipe(1L, "title", "desc", List.of("a"), List.of("b"), true, 2);

        assertThat(recipe.getId()).isEqualTo(1L);
        assertThat(recipe.getTitle()).isEqualTo("title");
        assertThat(recipe.getDescription()).isEqualTo("desc");
        assertThat(recipe.getIngredients()).containsExactly("a");
        assertThat(recipe.getInstructions()).containsExactly("b");
        assertThat(recipe.isVegetarian()).isTrue();
        assertThat(recipe.getServingSize()).isEqualTo(2);
    }

    @Test
    void shouldCreateRecipeWithPartialConstructor() {
        var recipe = new Recipe("title", "desc", List.of("a"), List.of("b"), true, 2);

        assertThat(recipe.getTitle()).isEqualTo("title");
        assertThat(recipe.getDescription()).isEqualTo("desc");
        assertThat(recipe.getIngredients()).containsExactly("a");
        assertThat(recipe.getInstructions()).containsExactly("b");
        assertThat(recipe.isVegetarian()).isTrue();
        assertThat(recipe.getServingSize()).isEqualTo(2);
    }

    @Test
    void shouldConvertToRecipeResponse() {
        var recipe = new Recipe(1L, "title", "desc", List.of("a"), List.of("b"), true, 2);
        var response = recipe.toResponse();

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("title");
        assertThat(response.description()).isEqualTo("desc");
        assertThat(response.ingredients()).containsExactly("a");
        assertThat(response.instructions()).containsExactly("b");
        assertThat(response.isVegetarian()).isTrue();
        assertThat(response.servingSize()).isEqualTo(2);
    }
}
