package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * Unit tests for ApiGlobalExceptionHandler.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiGlobalExceptionHandler Tests")
class ApiGlobalExceptionHandlerTest {

    private ApiGlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApiGlobalExceptionHandler();
    }

    @Nested
    @DisplayName("Given HttpMessageNotReadableException")
    class HttpMessageNotReadableExceptionTests {

        @Test
        @DisplayName("When handling HttpMessageNotReadableException, then should return BAD_REQUEST with problem details")
        void shouldReturnBadRequest_whenHandlingHttpMessageNotReadableException() {
            // Given
            var exception = mock(HttpMessageNotReadableException.class);
            when(exception.getMessage()).thenReturn("Malformed JSON");

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleHttpMessageNotReadable(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.getBody().getTitle()).isEqualTo("Bad Request");
            assertThat(result.getBody().getDetail()).isEqualTo("Invalid request body: malformed JSON or missing required fields");
        }

        @Test
        @DisplayName("When handling HttpMessageNotReadableException with null message, then should still return BAD_REQUEST")
        void shouldReturnBadRequest_whenHandlingHttpMessageNotReadableExceptionWithNullMessage() {
            // Given
            var exception = mock(HttpMessageNotReadableException.class);
            when(exception.getMessage()).thenReturn(null);

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleHttpMessageNotReadable(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("Given IllegalArgumentException")
    class IllegalArgumentExceptionTests {

        @Test
        @DisplayName("When handling IllegalArgumentException, then should return INTERNAL_SERVER_ERROR with problem details")
        void shouldReturnInternalServerError_whenHandlingIllegalArgumentException() {
            // Given
            var exception = new IllegalArgumentException("No handler found for command");

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleIllegalArgument(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.getBody().getTitle()).isEqualTo("Internal Server Error");
            assertThat(result.getBody().getDetail()).isEqualTo("Internal configuration error");
        }

        @Test
        @DisplayName("When handling IllegalArgumentException with null message, then should still return INTERNAL_SERVER_ERROR")
        void shouldReturnInternalServerError_whenHandlingIllegalArgumentExceptionWithNullMessage() {
            // Given
            var exception = new IllegalArgumentException((String) null);

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleIllegalArgument(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    @DisplayName("Given Unexpected Exception")
    class UnexpectedExceptionTests {

        @Test
        @DisplayName("When handling generic Exception, then should return INTERNAL_SERVER_ERROR with problem details")
        void shouldReturnInternalServerError_whenHandlingGenericException() {
            // Given
            var exception = new Exception("Unexpected error occurred");

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleUnexpectedException(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.getBody().getTitle()).isEqualTo("Internal Server Error");
            assertThat(result.getBody().getDetail()).isEqualTo("An unexpected error occurred");
        }

        @Test
        @DisplayName("When handling RuntimeException, then should return INTERNAL_SERVER_ERROR with problem details")
        void shouldReturnInternalServerError_whenHandlingRuntimeException() {
            // Given
            var exception = new RuntimeException("Runtime error");

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleUnexpectedException(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.getBody().getTitle()).isEqualTo("Internal Server Error");
            assertThat(result.getBody().getDetail()).isEqualTo("An unexpected error occurred");
        }

        @Test
        @DisplayName("When handling NullPointerException, then should return INTERNAL_SERVER_ERROR with problem details")
        void shouldReturnInternalServerError_whenHandlingNullPointerException() {
            // Given
            var exception = new NullPointerException("Null value encountered");

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleUnexpectedException(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("When handling exception with null message, then should still return INTERNAL_SERVER_ERROR")
        void shouldReturnInternalServerError_whenHandlingExceptionWithNullMessage() {
            // Given
            var exception = new Exception((String) null);

            // When
            ResponseEntity<ProblemDetail> result = exceptionHandler.handleUnexpectedException(exception);

            // Then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
