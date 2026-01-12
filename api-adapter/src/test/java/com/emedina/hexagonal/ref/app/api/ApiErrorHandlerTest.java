package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unit tests for ApiErrorHandler.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiErrorHandler Tests")
class ApiErrorHandlerTest {

    private ApiErrorHandler apiErrorHandler;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        apiErrorHandler = new ApiErrorHandler();
        mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/api/test");
    }

    @Nested
    @DisplayName("Given ValidationErrors")
    class ValidationErrorsTests {

        @Test
        @DisplayName("When mapping ValidationErrors with CannotBeNull, then should return BAD_REQUEST with appropriate detail")
        void shouldReturnBadRequest_whenMappingCannotBeNullValidationError() {
            // Given
            var validationError = new ValidationError.CannotBeNull("testObject");
            var error = new Error.ValidationErrors(List.of(validationError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Cannot be null [testObject]");
        }

        @Test
        @DisplayName("When mapping ValidationErrors with MustHaveContent, then should return BAD_REQUEST with appropriate detail")
        void shouldReturnBadRequest_whenMappingMustHaveContentValidationError() {
            // Given
            var validationError = new ValidationError.MustHaveContent("testField");
            var error = new Error.ValidationErrors(List.of(validationError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Must have content [testField]");
        }

        @Test
        @DisplayName("When mapping ValidationErrors with CannotBeEmpty, then should return BAD_REQUEST with appropriate detail")
        void shouldReturnBadRequest_whenMappingCannotBeEmptyValidationError() {
            // Given
            var validationError = new ValidationError.CannotBeEmpty(List.of("testList"));
            var error = new Error.ValidationErrors(List.of(validationError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Cannot be empty [[testList]]");
        }

        @Test
        @DisplayName("When mapping ValidationErrors with Invalid, then should return BAD_REQUEST with appropriate detail")
        void shouldReturnBadRequest_whenMappingInvalidValidationError() {
            // Given
            var validationError = new ValidationError.Invalid("testValue");
            var error = new Error.ValidationErrors(List.of(validationError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Invalid value provided [testValue]");
        }

        @Test
        @DisplayName("When mapping ValidationErrors with multiple errors, then should return BAD_REQUEST with combined details")
        void shouldReturnBadRequest_whenMappingMultipleValidationErrors() {
            // Given
            var validationError1 = new ValidationError.CannotBeNull("testObject");
            var validationError2 = new ValidationError.Invalid("testValue");
            var error = new Error.ValidationErrors(List.of(validationError1, validationError2));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Cannot be null [testObject]");
            assertThat(result.get(0).getDetail()).contains("Invalid value provided [testValue]");
        }
    }

    @Nested
    @DisplayName("Given BusinessError")
    class BusinessErrorTests {

        @Test
        @DisplayName("When mapping BusinessError.InvalidId, then should return BAD_REQUEST with appropriate detail")
        void shouldReturnBadRequest_whenMappingInvalidIdBusinessError() {
            // Given
            var error = new Error.BusinessError.InvalidId("test-id");

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).isEqualTo("Article id [test-id] not valid");
        }

        @Test
        @DisplayName("When mapping BusinessError.UnknownArticle, then should return NOT_FOUND with appropriate detail")
        void shouldReturnNotFound_whenMappingUnknownArticleBusinessError() {
            // Given
            var error = new Error.BusinessError.UnknownArticle("test-id");

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(result.get(0).getDetail()).isEqualTo("Article with id [test-id] not found");
        }
    }

    @Nested
    @DisplayName("Given TechnicalError")
    class TechnicalErrorTests {

        @Test
        @DisplayName("When mapping TechnicalError.SomethingWentWrong, then should return INTERNAL_SERVER_ERROR with appropriate detail")
        void shouldReturnInternalServerError_whenMappingSomethingWentWrongTechnicalError() {
            // Given
            var error = new Error.TechnicalError.SomethingWentWrong("Test error message");

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.get(0).getDetail()).isEqualTo("Internal server error [Test error message]");
            assertThat(result.get(0).getInstance()).isEqualTo(URI.create("/api/test"));
        }
    }

    @Nested
    @DisplayName("Given MultipleErrors")
    class MultipleErrorsTests {

        @Test
        @DisplayName("When mapping MultipleErrors with different error types, then should return list of problem details")
        void shouldReturnMultipleProblemDetails_whenMappingMultipleErrors() {
            // Given
            var businessError = new Error.BusinessError.InvalidId("test-id");
            var technicalError = new Error.TechnicalError.SomethingWentWrong("Test error message");
            var error = new Error.MultipleErrors(List.of(businessError, technicalError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).isEqualTo("Article id [test-id] not valid");
            assertThat(result.get(1).getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.get(1).getDetail()).isEqualTo("Internal server error [Test error message]");
        }

        @Test
        @DisplayName("When mapping MultipleErrors with nested MultipleErrors, then should flatten all errors")
        void shouldFlattenAllErrors_whenMappingNestedMultipleErrors() {
            // Given
            var validationError = new ValidationError.CannotBeNull("testObject");
            var validationErrors = new Error.ValidationErrors(List.of(validationError));
            var businessError = new Error.BusinessError.InvalidId("test-id");
            var nestedMultipleErrors = new Error.MultipleErrors(List.of(validationErrors, businessError));
            var technicalError = new Error.TechnicalError.SomethingWentWrong("Test error message");
            var error = new Error.MultipleErrors(List.of(nestedMultipleErrors, technicalError));

            // When
            List<ProblemDetail> result = apiErrorHandler.mapErrorToProblemDetail(error, mockRequest);

            // Then
            assertThat(result).hasSize(3);
            // First problem detail from ValidationErrors
            assertThat(result.get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(0).getDetail()).contains("Cannot be null [testObject]");
            // Second problem detail from BusinessError.InvalidId
            assertThat(result.get(1).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.get(1).getDetail()).isEqualTo("Article id [test-id] not valid");
            // Third problem detail from TechnicalError.SomethingWentWrong
            assertThat(result.get(2).getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(result.get(2).getDetail()).isEqualTo("Internal server error [Test error message]");
        }
    }

}
