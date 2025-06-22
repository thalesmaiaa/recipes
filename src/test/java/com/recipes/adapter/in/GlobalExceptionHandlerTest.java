package com.recipes.adapter.in;

import com.recipes.core.exceptions.RecipeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleDomainException() {
        var exception = new RecipeNotFoundException();
        var response = handler.handleDomainException(exception);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().details()).isEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Recipe not found");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("object", "field", "must not be null");
        var exception = new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        var response = handler.handleMethodArgumentNotValidException(exception);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("VALIDATION_EXCEPTION");
        assertThat(response.getBody().details()).containsExactly("field: must not be null");
    }
}
