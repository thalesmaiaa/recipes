package com.recipes.adapter.out.persistence;

import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        var recipe = RecipeFactory.createRecipe();

        var entity = RecipeEntity.fromDomain(recipe);

        assertThat(entity.getId()).isEqualTo(recipe.getId());
        assertThat(entity.getTitle()).isEqualTo(recipe.getTitle());
        assertThat(entity.getDescription()).isEqualTo(recipe.getDescription());
        assertThat(entity.getIngredients()).containsExactlyInAnyOrder(
                "spaghetti",
                "ground beef",
                "tomato sauce",
                "onion",
                "garlic",
                "olive oil");
        assertThat(entity.getInstructions()).containsExactlyInAnyOrder(
                "cook spaghetti according to package instructions.",
                "in a pan, heat olive oil and sauté onion and garlic.",
                "add ground beef and cook until browned.",
                "stir in tomato sauce and simmer for 20 minutes.",
                "serve sauce over spaghetti.");
        assertThat(entity.isVegetarian()).isEqualTo(recipe.isVegetarian());
        assertThat(entity.getServingSize()).isEqualTo(recipe.getServingSize());
    }

    @Test
    void shouldConvertFromEntityToDomain() {
        var entity = new RecipeEntity(
                2L,
                "Entity Title",
                "Entity Description",
                List.of("flour", "sugar"),
                List.of("Mix", "Bake"),
                false,
                4
        );

        var recipe = entity.toDomain();

        assertThat(recipe.getId()).isEqualTo(entity.getId());
        assertThat(recipe.getTitle()).isEqualTo(entity.getTitle());
        assertThat(recipe.getDescription()).isEqualTo(entity.getDescription());
        assertThat(recipe.getIngredients()).containsExactlyElementsOf(entity.getIngredients());
        assertThat(recipe.getInstructions()).containsExactlyElementsOf(entity.getInstructions());
        assertThat(recipe.isVegetarian()).isEqualTo(entity.isVegetarian());
        assertThat(recipe.getServingSize()).isEqualTo(entity.getServingSize());
    }
}
