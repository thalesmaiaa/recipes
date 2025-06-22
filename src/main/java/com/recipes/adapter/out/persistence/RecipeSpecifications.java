package com.recipes.adapter.out.persistence;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
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

    private static final int NOT_FOUND_IN_ARRAY = 0; // array_position returns 0 if not found
    private static final int FOUND_IN_ARRAY = 1;    // array_position returns 1+ if found

    public static Specification<RecipeEntity> buildRecipeSpecification(RecipeFiltersRequest filtersRequest) {
        return (root, query, cb) -> {
            var predicates = Stream.of(
                            includedIngredients(filtersRequest.includedIngredients(), root, cb),
                            excludedIngredients(filtersRequest.excludedIngredients(), root, cb),
                            matchesInstruction(filtersRequest.instruction(), root, cb),
                            isVegetarian(filtersRequest.vegetarian(), root, cb),
                            matchesServingSize(filtersRequest.servingSize(), root, cb)
                    )
                    .filter(Objects::nonNull)
                    .toList();

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate includedIngredients(List<String> includedIngredients, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (CollectionUtils.isEmpty(includedIngredients)) {
            return null;
        }

        var includedIngredientsPredicate = includedIngredients.stream()
                .map(ingredient -> cb.greaterThan(
                        generateArrayEqualsPredicate(root, cb, "ingredients", ingredient), NOT_FOUND_IN_ARRAY))
                .toList();
        return cb.and(includedIngredientsPredicate.toArray(new Predicate[0]));
    }

    private static Predicate excludedIngredients(List<String> excludedIngredients, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (CollectionUtils.isEmpty(excludedIngredients)) {
            return null;
        }

        var excludedIngredientsPredicate = excludedIngredients.stream()
                .map(ingredient -> cb.lessThan(
                        generateArrayEqualsPredicate(root, cb, "ingredients", ingredient), FOUND_IN_ARRAY)).toList();

        return cb.and(excludedIngredientsPredicate.toArray(new Predicate[0]));
    }

    private static Predicate matchesInstruction(String instruction, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(instruction) || instruction.isBlank()) {
            return null;
        }
        return cb.and(
                cb.greaterThan(generateArrayEqualsPredicate(root, cb, "instructions", instruction), NOT_FOUND_IN_ARRAY));
    }

    private static Predicate isVegetarian(Boolean isVegetarian, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(isVegetarian)) {
            return null;
        }
        return cb.equal(root.get("vegetarian"), isVegetarian);
    }

    private static Predicate matchesServingSize(Integer servingsSize, Root<RecipeEntity> root, CriteriaBuilder cb) {
        if (Objects.isNull(servingsSize)) {
            return null;
        }
        return cb.equal(root.get("servingSize"), servingsSize);
    }

    private static Expression<Integer> generateArrayEqualsPredicate(Root<RecipeEntity> root, CriteriaBuilder cb, String field, String value) {
        return cb.function("array_position", Integer.class, root.get(field), cb.literal(value));
    }

}
