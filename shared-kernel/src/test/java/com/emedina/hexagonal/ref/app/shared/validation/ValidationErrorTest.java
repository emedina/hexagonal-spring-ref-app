package com.emedina.hexagonal.ref.app.shared.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ValidationError hierarchy.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ValidationError Tests")
class ValidationErrorTest {

    @Nested
    @DisplayName("Given CannotBeNull")
    class CannotBeNullTests {

        @Test
        @DisplayName("When creating CannotBeNull with string object, then should store object correctly")
        void shouldCreateCannotBeNull_whenStringObjectProvided() {
            // Given
            String testObject = "test-string";

            // When
            ValidationError.CannotBeNull result = new ValidationError.CannotBeNull(testObject);

            // Then
            assertThat(result.obj()).isEqualTo(testObject);
            assertThat(result).isInstanceOf(ValidationError.class);
        }

        @Test
        @DisplayName("When creating CannotBeNull with null object, then should store null")
        void shouldCreateCannotBeNull_whenNullObjectProvided() {
            // Given
            Object nullObject = null;

            // When
            ValidationError.CannotBeNull result = new ValidationError.CannotBeNull(nullObject);

            // Then
            assertThat(result.obj()).isNull();
        }

        @Test
        @DisplayName("When creating CannotBeNull with complex object, then should store object correctly")
        void shouldCreateCannotBeNull_whenComplexObjectProvided() {
            // Given
            List<String> complexObject = Arrays.asList("item1", "item2");

            // When
            ValidationError.CannotBeNull result = new ValidationError.CannotBeNull(complexObject);

            // Then
            assertThat(result.obj()).isEqualTo(complexObject);
        }

        @Test
        @DisplayName("When creating CannotBeNull with number, then should store number correctly")
        void shouldCreateCannotBeNull_whenNumberProvided() {
            // Given
            Integer number = 42;

            // When
            ValidationError.CannotBeNull result = new ValidationError.CannotBeNull(number);

            // Then
            assertThat(result.obj()).isEqualTo(number);
        }
    }

    @Nested
    @DisplayName("Given CannotBeEmpty")
    class CannotBeEmptyTests {

        @Test
        @DisplayName("When creating CannotBeEmpty with empty list, then should store list correctly")
        void shouldCreateCannotBeEmpty_whenEmptyListProvided() {
            // Given
            List<String> emptyList = Collections.emptyList();

            // When
            ValidationError.CannotBeEmpty result = new ValidationError.CannotBeEmpty(emptyList);

            // Then
            assertThat(result.list()).isEqualTo(emptyList);
            assertThat(result.list()).isEmpty();
            assertThat(result).isInstanceOf(ValidationError.class);
        }

        @Test
        @DisplayName("When creating CannotBeEmpty with null list, then should store null")
        void shouldCreateCannotBeEmpty_whenNullListProvided() {
            // Given
            List<String> nullList = null;

            // When
            ValidationError.CannotBeEmpty result = new ValidationError.CannotBeEmpty(nullList);

            // Then
            assertThat(result.list()).isNull();
        }

        @Test
        @DisplayName("When creating CannotBeEmpty with non-empty list, then should store list correctly")
        void shouldCreateCannotBeEmpty_whenNonEmptyListProvided() {
            // Given
            List<String> nonEmptyList = Arrays.asList("item1", "item2", "item3");

            // When
            ValidationError.CannotBeEmpty result = new ValidationError.CannotBeEmpty(nonEmptyList);

            // Then
            assertThat(result.list()).isEqualTo(nonEmptyList);
            assertThat(result.list()).hasSize(3);
        }

        @Test
        @DisplayName("When creating CannotBeEmpty with single item list, then should store list correctly")
        void shouldCreateCannotBeEmpty_whenSingleItemListProvided() {
            // Given
            List<Integer> singleItemList = Collections.singletonList(123);

            // When
            ValidationError.CannotBeEmpty result = new ValidationError.CannotBeEmpty(singleItemList);

            // Then
            assertThat(result.list()).isEqualTo(singleItemList);
            assertThat(result.list()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Given MustHaveContent")
    class MustHaveContentTests {

        @Test
        @DisplayName("When creating MustHaveContent with field name, then should store name correctly")
        void shouldCreateMustHaveContent_whenFieldNameProvided() {
            // Given
            String fieldName = "username";

            // When
            ValidationError.MustHaveContent result = new ValidationError.MustHaveContent(fieldName);

            // Then
            assertThat(result.name()).isEqualTo(fieldName);
            assertThat(result).isInstanceOf(ValidationError.class);
        }

        @Test
        @DisplayName("When creating MustHaveContent with null name, then should store null")
        void shouldCreateMustHaveContent_whenNullNameProvided() {
            // Given
            String fieldName = null;

            // When
            ValidationError.MustHaveContent result = new ValidationError.MustHaveContent(fieldName);

            // Then
            assertThat(result.name()).isNull();
        }

        @Test
        @DisplayName("When creating MustHaveContent with empty name, then should store empty string")
        void shouldCreateMustHaveContent_whenEmptyNameProvided() {
            // Given
            String fieldName = "";

            // When
            ValidationError.MustHaveContent result = new ValidationError.MustHaveContent(fieldName);

            // Then
            assertThat(result.name()).isEqualTo(fieldName);
        }

        @Test
        @DisplayName("When creating MustHaveContent with unicode name, then should store unicode correctly")
        void shouldCreateMustHaveContent_whenUnicodeNameProvided() {
            // Given
            String fieldName = "用户名称";

            // When
            ValidationError.MustHaveContent result = new ValidationError.MustHaveContent(fieldName);

            // Then
            assertThat(result.name()).isEqualTo(fieldName);
        }
    }

    @Nested
    @DisplayName("Given Invalid")
    class InvalidTests {

        @Test
        @DisplayName("When creating Invalid with string value, then should store value correctly")
        void shouldCreateInvalid_whenStringValueProvided() {
            // Given
            String invalidValue = "invalid-format";

            // When
            ValidationError.Invalid result = new ValidationError.Invalid(invalidValue);

            // Then
            assertThat(result.value()).isEqualTo(invalidValue);
            assertThat(result).isInstanceOf(ValidationError.class);
        }

        @Test
        @DisplayName("When creating Invalid with null value, then should store null")
        void shouldCreateInvalid_whenNullValueProvided() {
            // Given
            String invalidValue = null;

            // When
            ValidationError.Invalid result = new ValidationError.Invalid(invalidValue);

            // Then
            assertThat(result.value()).isNull();
        }

        @Test
        @DisplayName("When creating Invalid with empty value, then should store empty string")
        void shouldCreateInvalid_whenEmptyValueProvided() {
            // Given
            String invalidValue = "";

            // When
            ValidationError.Invalid result = new ValidationError.Invalid(invalidValue);

            // Then
            assertThat(result.value()).isEqualTo(invalidValue);
        }

        @Test
        @DisplayName("When creating Invalid with special characters, then should store value correctly")
        void shouldCreateInvalid_whenSpecialCharactersProvided() {
            // Given
            String invalidValue = "invalid@#$%^&*()";

            // When
            ValidationError.Invalid result = new ValidationError.Invalid(invalidValue);

            // Then
            assertThat(result.value()).isEqualTo(invalidValue);
        }

        @Test
        @DisplayName("When creating Invalid with unicode value, then should store unicode correctly")
        void shouldCreateInvalid_whenUnicodeValueProvided() {
            // Given
            String invalidValue = "无效值 🚫";

            // When
            ValidationError.Invalid result = new ValidationError.Invalid(invalidValue);

            // Then
            assertThat(result.value()).isEqualTo(invalidValue);
        }
    }

    @Nested
    @DisplayName("Given ValidationError equality and toString")
    class ValidationErrorEqualityTests {

        @Test
        @DisplayName("When comparing same CannotBeNull errors, then should be equal")
        void shouldBeEqual_whenSameCannotBeNullErrorsCompared() {
            // Given
            String testObject = "test-object";
            ValidationError.CannotBeNull error1 = new ValidationError.CannotBeNull(testObject);
            ValidationError.CannotBeNull error2 = new ValidationError.CannotBeNull(testObject);

            // When & Then
            assertThat(error1).isEqualTo(error2);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("When comparing different CannotBeNull errors, then should not be equal")
        void shouldNotBeEqual_whenDifferentCannotBeNullErrorsCompared() {
            // Given
            ValidationError.CannotBeNull error1 = new ValidationError.CannotBeNull("object1");
            ValidationError.CannotBeNull error2 = new ValidationError.CannotBeNull("object2");

            // When & Then
            assertThat(error1).isNotEqualTo(error2);
        }

        @Test
        @DisplayName("When comparing same Invalid errors, then should be equal")
        void shouldBeEqual_whenSameInvalidErrorsCompared() {
            // Given
            String invalidValue = "invalid-value";
            ValidationError.Invalid error1 = new ValidationError.Invalid(invalidValue);
            ValidationError.Invalid error2 = new ValidationError.Invalid(invalidValue);

            // When & Then
            assertThat(error1).isEqualTo(error2);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("When comparing different types of ValidationErrors, then should not be equal")
        void shouldNotBeEqual_whenDifferentTypesCompared() {
            // Given
            ValidationError.Invalid invalidError = new ValidationError.Invalid("test");
            ValidationError.CannotBeNull cannotBeNullError = new ValidationError.CannotBeNull("test");

            // When & Then
            assertThat(invalidError).isNotEqualTo(cannotBeNullError);
        }

        @Test
        @DisplayName("When calling toString on ValidationErrors, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            ValidationError.Invalid invalidError = new ValidationError.Invalid("invalid-value");
            ValidationError.MustHaveContent mustHaveContentError = new ValidationError.MustHaveContent("field-name");
            ValidationError.CannotBeNull cannotBeNullError = new ValidationError.CannotBeNull("object");
            ValidationError.CannotBeEmpty cannotBeEmptyError = new ValidationError.CannotBeEmpty(Collections.emptyList());

            // When & Then
            assertThat(invalidError.toString()).contains("Invalid");
            assertThat(invalidError.toString()).contains("invalid-value");
            
            assertThat(mustHaveContentError.toString()).contains("MustHaveContent");
            assertThat(mustHaveContentError.toString()).contains("field-name");
            
            assertThat(cannotBeNullError.toString()).contains("CannotBeNull");
            assertThat(cannotBeNullError.toString()).contains("object");
            
            assertThat(cannotBeEmptyError.toString()).contains("CannotBeEmpty");
        }
    }
}
