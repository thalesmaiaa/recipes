package com.recipes.adapter.out.persistence;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.core.domain.Recipe;
import com.recipes.core.ports.out.RecipeAdapterPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecipeAdapterOut implements RecipeAdapterPortOut {

    private final RecipeRepository recipeRepository;

    public Page<Recipe> findAll(RecipeFiltersRequest filtersRequest, Pageable pageable) {
        var specification = RecipeSpecifications.buildRecipeSpecification(filtersRequest);
        return recipeRepository.findAll(specification, pageable).map(RecipeEntity::toDomain);
    }

    public Optional<Recipe> findRecipeById(Long id) {
        return recipeRepository.findById(id).map(RecipeEntity::toDomain);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        var recipeEntity = RecipeEntity.fromDomain(recipe);
        var createdEntity = recipeRepository.save(recipeEntity);
        return createdEntity.toDomain();
    }
}
