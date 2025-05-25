package com.emedina.hexagonal.ref.app.shared.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.vavr.control.Validation;

/**
 * Unit tests for Validations.
 *
 * @author Enrique Medina Montenegro
 */
class ValidationsTest {

    @Test
    void shouldReturnValidString_whenValidTextProvided() {
        // given
        String validText = "This is valid text";

        // when
        Validation<ValidationError, String> result = Validations.validateText(validText);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(validText);
    }

    @Test
    void shouldReturnInvalidValidation_whenNullTextProvided() {
        // given
        String nullText = null;

        // when
        Validation<ValidationError, String> result = Validations.validateText(nullText);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.Invalid.class);
        ValidationError.Invalid error = (ValidationError.Invalid) result.getError();
        assertThat(error.value()).isNull();
    }

    @Test
    void shouldReturnInvalidValidation_whenEmptyTextProvided() {
        // given
        String emptyText = "";

        // when
        Validation<ValidationError, String> result = Validations.validateText(emptyText);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.Invalid.class);
        ValidationError.Invalid error = (ValidationError.Invalid) result.getError();
        assertThat(error.value()).isEqualTo(emptyText);
    }

    @Test
    void shouldReturnValidString_whenWhitespaceTextProvided() {
        // given
        String whitespaceText = "   ";

        // when
        Validation<ValidationError, String> result = Validations.validateText(whitespaceText);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(whitespaceText);
    }

    @Test
    void shouldReturnValidString_whenTextWithSpecialCharactersProvided() {
        // given
        String specialText = "Text with special chars: @#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";

        // when
        Validation<ValidationError, String> result = Validations.validateText(specialText);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(specialText);
    }

    @Test
    void shouldReturnValidLong_whenPositiveNumberProvided() {
        // given
        Long positiveNumber = 42L;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(positiveNumber);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(positiveNumber);
    }

    @Test
    void shouldReturnValidLong_whenNumberEqualsOneProvided() {
        // given
        Long numberOne = 1L;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(numberOne);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(numberOne);
    }

    @Test
    void shouldReturnInvalidValidation_whenZeroProvided() {
        // given
        Long zero = 0L;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(zero);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeNull.class);
        ValidationError.CannotBeNull error = (ValidationError.CannotBeNull) result.getError();
        assertThat(error.obj()).isEqualTo(zero);
    }

    @Test
    void shouldReturnInvalidValidation_whenNegativeNumberProvided() {
        // given
        Long negativeNumber = -5L;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(negativeNumber);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeNull.class);
        ValidationError.CannotBeNull error = (ValidationError.CannotBeNull) result.getError();
        assertThat(error.obj()).isEqualTo(negativeNumber);
    }

    @Test
    void shouldReturnInvalidValidation_whenNullNumberProvided() {
        // given
        Long nullNumber = null;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(nullNumber);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeNull.class);
        ValidationError.CannotBeNull error = (ValidationError.CannotBeNull) result.getError();
        assertThat(error.obj()).isNull();
    }

    @Test
    void shouldReturnValidInputStream_whenValidStreamWithContentProvided() {
        // given
        String content = "This is test content";
        InputStream validStream = new ByteArrayInputStream(content.getBytes());
        String name = "test-stream";

        // when
        Validation<ValidationError, InputStream> result = Validations.validateContent(validStream, name);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(validStream);
    }

    @Test
    void shouldReturnInvalidValidation_whenNullStreamProvided() {
        // given
        InputStream nullStream = null;
        String name = "test-stream";

        // when
        Validation<ValidationError, InputStream> result = Validations.validateContent(nullStream, name);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.MustHaveContent.class);
        ValidationError.MustHaveContent error = (ValidationError.MustHaveContent) result.getError();
        assertThat(error.name()).isEqualTo(name);
    }

    @Test
    void shouldReturnInvalidValidation_whenEmptyStreamProvided() {
        // given
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        String name = "empty-stream";

        // when
        Validation<ValidationError, InputStream> result = Validations.validateContent(emptyStream, name);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.MustHaveContent.class);
        ValidationError.MustHaveContent error = (ValidationError.MustHaveContent) result.getError();
        assertThat(error.name()).isEqualTo(name);
    }

    @Test
    void shouldReturnValidList_whenNonEmptyListProvided() {
        // given
        List<String> nonEmptyList = Arrays.asList("item1", "item2", "item3");

        // when
        Validation<ValidationError, List> result = Validations.validateNotEmpty(nonEmptyList);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(nonEmptyList);
    }

    @Test
    void shouldReturnValidList_whenSingleItemListProvided() {
        // given
        List<String> singleItemList = Arrays.asList("single-item");

        // when
        Validation<ValidationError, List> result = Validations.validateNotEmpty(singleItemList);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(singleItemList);
    }

    @Test
    void shouldReturnInvalidValidation_whenEmptyListProvided() {
        // given
        List<String> emptyList = Collections.emptyList();

        // when
        Validation<ValidationError, List> result = Validations.validateNotEmpty(emptyList);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeEmpty.class);
        ValidationError.CannotBeEmpty error = (ValidationError.CannotBeEmpty) result.getError();
        assertThat(error.list()).isEqualTo(emptyList);
    }

    @Test
    void shouldReturnInvalidValidation_whenNullListProvided() {
        // given
        List<String> nullList = null;

        // when
        Validation<ValidationError, List> result = Validations.validateNotEmpty(nullList);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeEmpty.class);
        ValidationError.CannotBeEmpty error = (ValidationError.CannotBeEmpty) result.getError();
        assertThat(error.list()).isNull();
    }

    @Test
    void shouldReturnValidObject_whenNonNullObjectProvided() {
        // given
        String nonNullObject = "test-object";

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(nonNullObject);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(nonNullObject);
    }

    @Test
    void shouldReturnValidObject_whenComplexObjectProvided() {
        // given
        List<String> complexObject = Arrays.asList("item1", "item2");

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(complexObject);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(complexObject);
    }

    @Test
    void shouldReturnInvalidValidation_whenNullObjectProvided() {
        // given
        Object nullObject = null;

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(nullObject);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(ValidationError.CannotBeNull.class);
        ValidationError.CannotBeNull error = (ValidationError.CannotBeNull) result.getError();
        assertThat(error.obj()).isNull();
    }

    @Test
    void shouldReturnValidObject_whenEmptyStringProvidedToMandatory() {
        // given
        String emptyString = "";

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(emptyString);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(emptyString);
    }

    @Test
    void shouldReturnValidObject_whenZeroProvidedToMandatory() {
        // given
        Integer zero = 0;

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(zero);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(zero);
    }

    @Test
    void shouldReturnValidObject_whenFalseProvidedToMandatory() {
        // given
        Boolean falseValue = false;

        // when
        Validation<ValidationError, Object> result = Validations.validateMandatory(falseValue);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(falseValue);
    }

    @Test
    void shouldReturnValidString_whenUnicodeTextProvided() {
        // given
        String unicodeText = "Unicode text: 你好世界 🌍 Здравствуй мир";

        // when
        Validation<ValidationError, String> result = Validations.validateText(unicodeText);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(unicodeText);
    }

    @Test
    void shouldReturnValidLong_whenMaxLongValueProvided() {
        // given
        Long maxValue = Long.MAX_VALUE;

        // when
        Validation<ValidationError, Long> result = Validations.validateGreaterThanZero(maxValue);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(maxValue);
    }
}
