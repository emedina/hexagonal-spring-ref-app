package com.emedina.hexagonal.ref.app.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for the API layer.
 * <p>
 * Catches exceptions that escape the functional error handling (Either) and converts them
 * to RFC 7807 Problem Details responses.
 *
 * @author Enrique Medina Montenegro
 */
@Slf4j
@ControllerAdvice
final class ApiGlobalExceptionHandler {

    /**
     * Handles malformed JSON or deserialization errors.
     *
     * @param ex the exception thrown by Spring when the request body cannot be read
     * @return a 400 Bad Request response with problem details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex) {
        log.atWarn().setMessage("Invalid request body received").setCause(ex).log();

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request body: malformed JSON or missing required fields"
        );
        problemDetail.setTitle("Bad Request");

        return ResponseEntity.badRequest().body(problemDetail);
    }

    /**
     * Handles illegal argument exceptions, typically from missing handlers in the bus registry.
     *
     * @param ex the exception thrown when an invalid argument is provided
     * @return a 500 Internal Server Error response with problem details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgument(final IllegalArgumentException ex) {
        log.atError().setMessage("Configuration error detected").setCause(ex).log();

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal configuration error"
        );
        problemDetail.setTitle("Internal Server Error");

        return ResponseEntity.internalServerError().body(problemDetail);
    }

    /**
     * Catches all unhandled exceptions as a last resort.
     *
     * @param ex the unexpected exception
     * @return a 500 Internal Server Error response with problem details
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleUnexpectedException(final Exception ex) {
        log.atError().setMessage("Unexpected error occurred").setCause(ex).log();

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
        problemDetail.setTitle("Internal Server Error");

        return ResponseEntity.internalServerError().body(problemDetail);
    }

}
