package com.recipes.core.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class RecipeNotFoundException extends DomainException {

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    public List<String> getDetails() {
        return List.of();
    }
}
