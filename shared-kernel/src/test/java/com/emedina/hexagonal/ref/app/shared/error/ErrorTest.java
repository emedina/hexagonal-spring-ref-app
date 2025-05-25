package com.emedina.hexagonal.ref.app.shared.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

/**
 * Unit tests for Error hierarchy.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("Error Tests")
class ErrorTest {

    @Nested
    @DisplayName("Given ValidationErrors")
    class ValidationErrorsTests {

        @Test
        @DisplayName("When creating ValidationErrors with single error, then should store error correctly")
        void shouldCreateValidationErrors_whenSingleErrorProvided() {
            // Given
            ValidationError validationError = new ValidationError.Invalid("test-value");
            List<ValidationError> errors = Collections.singletonList(validationError);

            // When
            Error.ValidationErrors result = new Error.ValidationErrors(errors);

            // Then
            assertThat(result.errors()).hasSize(1);
            assertThat(result.errors().get(0)).isEqualTo(validationError);
            assertThat(result).isInstanceOf(Error.class);
        }

        @Test
        @DisplayName("When creating ValidationErrors with multiple errors, then should store all errors correctly")
        void shouldCreateValidationErrors_whenMultipleErrorsProvided() {
            // Given
            ValidationError error1 = new ValidationError.Invalid("value1");
            ValidationError error2 = new ValidationError.MustHaveContent("field1");
            ValidationError error3 = new ValidationError.CannotBeNull("object1");
            List<ValidationError> errors = Arrays.asList(error1, error2, error3);

            // When
            Error.ValidationErrors result = new Error.ValidationErrors(errors);

            // Then
            assertThat(result.errors()).hasSize(3);
            assertThat(result.errors()).containsExactly(error1, error2, error3);
        }

        @Test
        @DisplayName("When creating ValidationErrors with empty list, then should store empty list")
        void shouldCreateValidationErrors_whenEmptyListProvided() {
            // Given
            List<ValidationError> errors = Collections.emptyList();

            // When
            Error.ValidationErrors result = new Error.ValidationErrors(errors);

            // Then
            assertThat(result.errors()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Given MultipleErrors")
    class MultipleErrorsTests {

        @Test
        @DisplayName("When creating MultipleErrors with single error, then should store error correctly")
        void shouldCreateMultipleErrors_whenSingleErrorProvided() {
            // Given
            Error error = new Error.BusinessError.UnknownArticle("article-123");
            List<Error> errors = Collections.singletonList(error);

            // When
            Error.MultipleErrors result = new Error.MultipleErrors(errors);

            // Then
            assertThat(result.errors()).hasSize(1);
            assertThat(result.errors().get(0)).isEqualTo(error);
            assertThat(result).isInstanceOf(Error.class);
        }

        @Test
        @DisplayName("When creating MultipleErrors with mixed error types, then should store all errors correctly")
        void shouldCreateMultipleErrors_whenMixedErrorTypesProvided() {
            // Given
            Error businessError = new Error.BusinessError.InvalidId("invalid-id");
            Error technicalError = new Error.TechnicalError.SomethingWentWrong("System failure");
            Error validationErrors = new Error.ValidationErrors(Collections.singletonList(new ValidationError.Invalid(
                "test")));
            List<Error> errors = Arrays.asList(businessError, technicalError, validationErrors);

            // When
            Error.MultipleErrors result = new Error.MultipleErrors(errors);

            // Then
            assertThat(result.errors()).hasSize(3);
            assertThat(result.errors()).containsExactly(businessError, technicalError, validationErrors);
        }
    }

    @Nested
    @DisplayName("Given BusinessError")
    class BusinessErrorTests {

        @Test
        @DisplayName("When creating UnknownArticle error, then should store id correctly")
        void shouldCreateUnknownArticleError_whenIdProvided() {
            // Given
            String articleId = "article-123";

            // When
            Error.BusinessError.UnknownArticle result = new Error.BusinessError.UnknownArticle(articleId);

            // Then
            assertThat(result.id()).isEqualTo(articleId);
            assertThat(result).isInstanceOf(Error.BusinessError.class);
            assertThat(result).isInstanceOf(Error.class);
        }

        @Test
        @DisplayName("When creating UnknownArticle error with null id, then should store null")
        void shouldCreateUnknownArticleError_whenNullIdProvided() {
            // Given
            String articleId = null;

            // When
            Error.BusinessError.UnknownArticle result = new Error.BusinessError.UnknownArticle(articleId);

            // Then
            assertThat(result.id()).isNull();
        }

        @Test
        @DisplayName("When creating InvalidId error, then should store id correctly")
        void shouldCreateInvalidIdError_whenIdProvided() {
            // Given
            String invalidId = "invalid-id-format";

            // When
            Error.BusinessError.InvalidId result = new Error.BusinessError.InvalidId(invalidId);

            // Then
            assertThat(result.id()).isEqualTo(invalidId);
            assertThat(result).isInstanceOf(Error.BusinessError.class);
            assertThat(result).isInstanceOf(Error.class);
        }

        @Test
        @DisplayName("When creating InvalidId error with empty string, then should store empty string")
        void shouldCreateInvalidIdError_whenEmptyStringProvided() {
            // Given
            String invalidId = "";

            // When
            Error.BusinessError.InvalidId result = new Error.BusinessError.InvalidId(invalidId);

            // Then
            assertThat(result.id()).isEqualTo(invalidId);
        }
    }

    @Nested
    @DisplayName("Given TechnicalError")
    class TechnicalErrorTests {

        @Test
        @DisplayName("When creating SomethingWentWrong error, then should store message correctly")
        void shouldCreateSomethingWentWrongError_whenMessageProvided() {
            // Given
            String errorMessage = "Database connection failed";

            // When
            Error.TechnicalError.SomethingWentWrong result = new Error.TechnicalError.SomethingWentWrong(errorMessage);

            // Then
            assertThat(result.message()).isEqualTo(errorMessage);
            assertThat(result).isInstanceOf(Error.TechnicalError.class);
            assertThat(result).isInstanceOf(Error.class);
        }

        @Test
        @DisplayName("When creating SomethingWentWrong error with null message, then should store null")
        void shouldCreateSomethingWentWrongError_whenNullMessageProvided() {
            // Given
            String errorMessage = null;

            // When
            Error.TechnicalError.SomethingWentWrong result = new Error.TechnicalError.SomethingWentWrong(errorMessage);

            // Then
            assertThat(result.message()).isNull();
        }

        @Test
        @DisplayName("When creating SomethingWentWrong error with unicode message, then should store unicode correctly")
        void shouldCreateSomethingWentWrongError_whenUnicodeMessageProvided() {
            // Given
            String errorMessage = "Erreur système: 系统错误 🚨";

            // When
            Error.TechnicalError.SomethingWentWrong result = new Error.TechnicalError.SomethingWentWrong(errorMessage);

            // Then
            assertThat(result.message()).isEqualTo(errorMessage);
        }
    }

    @Nested
    @DisplayName("Given Error equality and toString")
    class ErrorEqualityTests {

        @Test
        @DisplayName("When comparing same ValidationErrors, then should be equal")
        void shouldBeEqual_whenSameValidationErrorsCompared() {
            // Given
            List<ValidationError> errors = Arrays.asList(new ValidationError.Invalid("test"));
            Error.ValidationErrors error1 = new Error.ValidationErrors(errors);
            Error.ValidationErrors error2 = new Error.ValidationErrors(errors);

            // When & Then
            assertThat(error1).isEqualTo(error2);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("When comparing same BusinessErrors, then should be equal")
        void shouldBeEqual_whenSameBusinessErrorsCompared() {
            // Given
            Error.BusinessError.UnknownArticle error1 = new Error.BusinessError.UnknownArticle("article-123");
            Error.BusinessError.UnknownArticle error2 = new Error.BusinessError.UnknownArticle("article-123");

            // When & Then
            assertThat(error1).isEqualTo(error2);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("When comparing different BusinessErrors, then should not be equal")
        void shouldNotBeEqual_whenDifferentBusinessErrorsCompared() {
            // Given
            Error.BusinessError.UnknownArticle error1 = new Error.BusinessError.UnknownArticle("article-123");
            Error.BusinessError.UnknownArticle error2 = new Error.BusinessError.UnknownArticle("article-456");

            // When & Then
            assertThat(error1).isNotEqualTo(error2);
        }

        @Test
        @DisplayName("When calling toString on errors, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            Error.BusinessError.UnknownArticle businessError = new Error.BusinessError.UnknownArticle("article-123");
            Error.TechnicalError.SomethingWentWrong technicalError = new Error.TechnicalError.SomethingWentWrong(
                "System error");

            // When & Then
            assertThat(businessError.toString()).contains("UnknownArticle");
            assertThat(businessError.toString()).contains("article-123");
            assertThat(technicalError.toString()).contains("SomethingWentWrong");
            assertThat(technicalError.toString()).contains("System error");
        }
    }
}
