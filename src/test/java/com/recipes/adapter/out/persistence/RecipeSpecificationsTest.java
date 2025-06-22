package com.recipes.adapter.out.persistence;

import com.recipes.adapter.in.dto.request.RecipeFiltersRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecipeSpecificationsTest {

    @Test
    void shouldBuildSpecificationWithAllFilters() {
        var filters = new RecipeFiltersRequest(
                List.of("egg", "milk"),
                "bake",
                true,
                4
        );
        var root = mock(Root.class);
        var cb = mock(CriteriaBuilder.class);
        var p1 = mock(Predicate.class);
        var p2 = mock(Predicate.class);
        var p3 = mock(Predicate.class);
        var p4 = mock(Predicate.class);

        when(cb.greaterThan(any(), eq(0))).thenReturn(p1, p2, p3);
        when(cb.function(any(), eq(Integer.class), any(), any())).thenReturn(mock(jakarta.persistence.criteria.Expression.class));
        when(cb.and(any(Predicate[].class))).thenReturn(p1, p2, p3, p4);

        when(cb.equal(any(), any())).thenReturn(p3, p4);

        var spec = RecipeSpecifications.buildRecipeSpecification(filters);
        var result = spec.toPredicate(root, null, cb);

        assertThat(result).isNotNull();
        verify(cb, atLeastOnce()).and(any());
    }

    @Test
    void shouldBuildSpecificationWithNoFilters() {
        var filters = new RecipeFiltersRequest(null, null, null, null);
        var root = mock(Root.class);
        var cb = mock(CriteriaBuilder.class);
        var p1 = mock(Predicate.class);

        when(cb.and()).thenReturn(p1);

        var spec = RecipeSpecifications.buildRecipeSpecification(filters);
        var result = spec.toPredicate(root, null, cb);

        assertThat(result).isNotNull();
        verify(cb, atLeastOnce()).and();
    }

    @Test
    void shouldBuildSpecificationWithSomeFilters() {
        var filters = new RecipeFiltersRequest(List.of("egg"), null, null, null);
        var root = mock(Root.class);
        var cb = mock(CriteriaBuilder.class);
        var p1 = mock(Predicate.class);

        when(cb.greaterThan(any(), eq(0))).thenReturn(p1);
        when(cb.function(any(), eq(Integer.class), any(), any())).thenReturn(mock(jakarta.persistence.criteria.Expression.class));
        when(cb.and(any(Predicate[].class))).thenReturn(p1);

        var spec = RecipeSpecifications.buildRecipeSpecification(filters);
        var result = spec.toPredicate(root, null, cb);

        assertThat(result).isNotNull();
        verify(cb, atLeastOnce()).and(any());
    }

    @Test
    void shouldBuildSpecificationWithEmptyInstruction() {
        var filters = new RecipeFiltersRequest(null, "", null, null);
        var root = mock(Root.class);
        var cb = mock(CriteriaBuilder.class);
        var p1 = mock(Predicate.class);

        when(cb.and()).thenReturn(p1);

        var spec = RecipeSpecifications.buildRecipeSpecification(filters);
        var result = spec.toPredicate(root, null, cb);

        assertThat(result).isNotNull();
        verify(cb, atLeastOnce()).and();
    }
}
