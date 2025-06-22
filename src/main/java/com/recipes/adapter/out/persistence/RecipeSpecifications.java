package com.recipes.adapter.out.persistence;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeSpecifications {

    public static Specification<RecipeEntity> buildRecipeSpecification(RecipeFiltersRequest filtersRequest) {
        return (root, query, cb) -> {
            var predicates = Stream.of(
                    matchesIngredients(filtersRequest.ingredients(), root, cb),
                    matchesInstruction(filtersRequest.instruction(), root, cb),
                    isVegetarian(filtersRequest.vegetarian(), root, cb),
                    matchesServingSize(filtersRequest.servingSize(), root, cb)
            ).filter(Objects::nonNull).toList();

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate matchesIngredients(List<String> ingredients, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (CollectionUtils.isEmpty(ingredients)) return null;

        var ingredientPredicates = ingredients.stream().map(ingredient -> getPredicate(root, cb, "ingredients", ingredient)).toList();
        return cb.and(ingredientPredicates.toArray(new Predicate[0]));
    }

    private static Predicate matchesInstruction(String instruction, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(instruction) || instruction.isBlank()) return null;
        return cb.and(getPredicate(root, cb, "instructions", instruction));
    }

    private static Predicate isVegetarian(Boolean isVegetarian, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(isVegetarian)) return null;
        return cb.equal(root.get("vegetarian"), isVegetarian);
    }

    private static Predicate matchesServingSize(Integer servingsSize, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(servingsSize)) return null;
        return cb.equal(root.get("servingSize"), servingsSize);
    }

    private static Predicate getPredicate(Root<RecipeEntity> root, CriteriaBuilder cb, String field, String value) {
        var noneMatchingValue = 0;
        return cb.greaterThan(cb.function(
                "array_position",
                Integer.class,
                root.get(field),
                cb.literal(value)
        ), noneMatchingValue);
    }

}
