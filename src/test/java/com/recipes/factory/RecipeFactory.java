package com.recipes.factory;

import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.domain.Recipe;

import java.util.List;

public class RecipeFactory {

    public static Recipe createRecipe() {
        return new Recipe(
                1L,
                "Spaghetti Bolognese",
                "A classic Italian pasta dish with a rich meat sauce.",
                List.of("Spaghetti", "Ground beef", "Tomato sauce", "Onion", "Garlic", "Olive oil"),
                List.of("Cook spaghetti according to package instructions.",
                        "In a pan, heat olive oil and sauté onion and garlic.",
                        "Add ground beef and cook until browned.",
                        "Stir in tomato sauce and simmer for 20 minutes.",
                        "Serve sauce over spaghetti."),
                false,
                4);
    }

    public static Recipe createVegetarianRecipe() {
        return new Recipe(
                1L,
                "Vegetable Stir Fry",
                "A quick and healthy stir fry with mixed vegetables.",
                List.of("Broccoli", "Carrot", "Bell pepper", "Soy sauce", "Ginger", "Garlic"),
                List.of("Heat oil in a pan.",
                        "Add ginger and garlic, sauté for 1 minute.",
                        "Add vegetables and stir fry for 5-7 minutes.",
                        "Add soy sauce and cook for another 2 minutes."),
                true,
                2);
    }

    public static RecipeResponse generateRecipeResponse(Recipe recipe) {
        return recipe.toResponse();
    }
}
