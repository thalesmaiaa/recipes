package com.recipes.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.AbstractIntegrationTest;
import com.recipes.adapter.in.dto.response.RecipeResponse;
import com.recipes.adapter.out.persistence.RecipeEntity;
import com.recipes.adapter.out.persistence.RecipeRepository;
import com.recipes.factory.RecipeFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeControllerAdapterIT extends AbstractIntegrationTest {

    private static final String PATH = "/v1/recipes";

    @Autowired
    RecipeRepository recipeRepository;

    private RecipeEntity mockRecipeEntity;

    @BeforeEach
    void setUp() {
        var recipeEntity = RecipeEntity.fromDomain(RecipeFactory.createRecipe());
        recipeEntity.setId(null);
        mockRecipeEntity = recipeRepository.save(recipeEntity);
    }

    @AfterEach
    void clean() {
        recipeRepository.deleteAll();
    }

    @Test
    void shouldCreateRecipe() {
        var request = RecipeFactory.buildCreateRecipeRequest();
        var response = restTemplate.postForEntity(getUrl(PATH), request, ResponseEntity.class);
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void shouldDeleteRecipe() {
        var response = restTemplate.exchange(getUrl(PATH + "/%d".formatted(mockRecipeEntity.getId())), HttpMethod.DELETE, null, ResponseEntity.class);
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(recipeRepository.findById(mockRecipeEntity.getId())).isEmpty();
    }

    @Test
    void shouldFindRecipeById() {
        var response = restTemplate.getForEntity(getUrl(PATH) + "/%d".formatted(mockRecipeEntity.getId()), RecipeResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getClass()).isEqualTo(RecipeFactory.generateRecipeResponse(mockRecipeEntity.toDomain()).getClass());
    }

    @Test
    void shouldFindRecipes() {
        var mapper = new ObjectMapper();
        var response = restTemplate.exchange(
                getUrl(PATH),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        var body = response.getBody();
        var recipes = mapper.convertValue(body.get("content"), RecipeResponse[].class);
        var recipesList = Arrays.stream(recipes).toList();
        assertThat(body).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recipesList.get(0)).isEqualTo(RecipeFactory.generateRecipeResponse(mockRecipeEntity.toDomain()));
    }

    @Test
    void shouldFindRecipesWithFilters() {
        var vegetarianRecipe = RecipeFactory.createVegetarianRecipe();
        vegetarianRecipe.setId(null);
        var commonRecipe = RecipeFactory.createRecipe();
        commonRecipe.setId(null);

        recipeRepository.saveAll(List.of(RecipeEntity.fromDomain(vegetarianRecipe), RecipeEntity.fromDomain(commonRecipe)));

        var url = getUrl(PATH) + "?vegetarian=true&includedIngredients=carrot";
        var mapper = new ObjectMapper();
        var response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var recipes = mapper.convertValue(body.get("content"), RecipeResponse[].class);
        var recipesList = Arrays.stream(recipes).toList();

        assertThat(recipesList).hasSize(1);
        assertThat(recipesList.get(0).title()).isEqualTo(vegetarianRecipe.getTitle());
        assertThat(recipesList.get(0).vegetarian()).isTrue();
        assertThat(recipesList.get(0).ingredients()).contains("carrot");
        assertThat(recipesList.get(0).description()).isEqualTo(vegetarianRecipe.getDescription());
    }

    @Test
    void shouldUpdateRecipe() {
        var updateRequest = RecipeFactory.buildUpdateRecipeRequest();

        var response = restTemplate.exchange(
                getUrl(PATH + "/%d".formatted(mockRecipeEntity.getId())),
                HttpMethod.PATCH,
                new org.springframework.http.HttpEntity<>(updateRequest),
                RecipeResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo(updateRequest.title());
        assertThat(response.getBody().description()).isEqualTo(updateRequest.description());
        assertThat(response.getBody().ingredients()).isEqualTo(updateRequest.ingredients());
        assertThat(response.getBody().instructions()).isEqualTo(updateRequest.instructions());
    }

}
