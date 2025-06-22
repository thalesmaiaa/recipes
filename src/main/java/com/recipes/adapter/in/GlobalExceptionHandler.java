package com.recipes.adapter.in;

import com.recipes.core.exceptions.DomainException;
import com.recipes.core.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> handleDomainException(DomainException exception) {
        var exceptionResponse = new ExceptionResponse(exception.getMessage(), exception.getDetails());
        return new ResponseEntity<>(exceptionResponse, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fieldErrors = e.getFieldErrors()
                .stream()
                .map(this::formatFieldErrorMessage)
                .toList();

        var exceptionResponse = new ExceptionResponse("VALIDATION_EXCEPTION", fieldErrors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private String formatFieldErrorMessage(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }


}
