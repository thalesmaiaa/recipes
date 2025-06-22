package com.recipes.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public abstract class DomainException extends RuntimeException {
    protected List<String> details;
    protected HttpStatus httpStatus;

    public DomainException(String message) {
        super(message);
    }
}
