package com.recipes.adapter.out.persistence;

import com.recipes.core.domain.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(columnDefinition = "text[]")
    private List<String> ingredients;

    @Column(columnDefinition = "text[]")
    private List<String> instructions;

    private boolean vegetarian;

    private Integer servingSize;

    public static RecipeEntity fromDomain(Recipe recipe) {
        return new RecipeEntity(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getIngredients().stream().map(String::toLowerCase).distinct().toList(),
                recipe.getInstructions().stream().map(String::toLowerCase).distinct().toList(),
                recipe.isVegetarian(),
                recipe.getServingSize()
        );
    }

    public Recipe toDomain() {
        return new Recipe(
                this.id,
                this.title,
                this.description,
                this.ingredients,
                this.instructions,
                this.vegetarian,
                this.servingSize
        );
    }

}
