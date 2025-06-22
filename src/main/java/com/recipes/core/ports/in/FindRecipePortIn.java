package com.recipes.core.ports.in;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import com.recipes.adapter.in.dto.response.RecipeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindRecipePortIn {

    RecipeResponse execute(Long id);

    Page<RecipeResponse> execute(RecipeFiltersRequest filtersRequest, Pageable pageable);
}
