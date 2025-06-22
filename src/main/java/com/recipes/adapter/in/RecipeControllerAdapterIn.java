package com.recipes.adapter.in;

import com.recipes.adapter.in.dto.request.CreateRecipeRequest;
import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.adapter.in.dto.request.UpdateRecipeRequest;
import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.core.ports.in.CreateRecipePortIn;
import com.recipes.core.ports.in.DeleteRecipePortIn;
import com.recipes.core.ports.in.FindRecipePortIn;
import com.recipes.core.ports.in.UpdateRecipePortIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipes")
public class RecipeControllerAdapterIn {

    private final UpdateRecipePortIn updateRecipePortIn;
    private final CreateRecipePortIn createRecipePortIn;
    private final DeleteRecipePortIn deleteRecipePortIn;
    private final FindRecipePortIn findRecipeById;
    private final FindRecipePortIn findRecipePortIn;

    @PostMapping
    public ResponseEntity<Void> createRecipe(@RequestBody @Valid CreateRecipeRequest createRecipeRequest) {
        log.info("RecipeControllerAdapterIn.createRecipe - payload: [{}]", createRecipeRequest.toString());
        var createdRecipe = createRecipePortIn.execute(createRecipeRequest.toDomain());
        var resourceLocation = URI.create("/v1/recipes/" + createdRecipe.id());
        return ResponseEntity.created(resourceLocation).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        log.info("RecipeControllerAdapterIn.deleteRecipe - recipeId: [{}]", id);
        deleteRecipePortIn.execute(id);
    }

    @GetMapping("/{id}")
    public RecipeResponse findRecipeById(@PathVariable Long id) {
        log.debug("RecipeControllerAdapterIn.findRecipeById - recipeId: [{}]", id);
        return findRecipeById.execute(id);
    }

    @GetMapping
    public Page<RecipeResponse> findRecipes(@ModelAttribute RecipeFiltersRequest filtersRequest, Pageable pageable) {
        return findRecipePortIn.execute(filtersRequest, pageable);
    }

    @PatchMapping("/{id}")
    public RecipeResponse updateRecipe(@PathVariable Long id, @RequestBody @Valid UpdateRecipeRequest request) {
        log.info("RecipeControllerAdapterIn.updateRecipe - payload: [{}], recipeId: [{}]", request.toString(), id);
        return updateRecipePortIn.execute(request.toDomain(id));
    }
}
