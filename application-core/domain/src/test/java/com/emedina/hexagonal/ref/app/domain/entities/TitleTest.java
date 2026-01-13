package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for Title.
 *
 * @author Enrique Medina Montenegro
 */
class TitleTest {

    @Test
    void shouldCreateValidTitle_whenValidStringProvided() {
        // given
        String validTitle = "Introduction to Hexagonal Architecture";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(validTitle);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(validTitle);
    }

    @Test
    void shouldReturnValidationError_whenNullTitleProvided() {
        // given
        String nullTitle = null;

        // when
        Validation<Error, Title> result = Title.validateThenCreate(nullTitle);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenEmptyTitleProvided() {
        // given
        String emptyTitle = "";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(emptyTitle);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateTitleWithSpecialCharacters_whenValidStringWithSpecialCharsProvided() {
        // given
        String titleWithSpecialChars = "Clean Architecture: A Craftsman's Guide to Software Structure & Design";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(titleWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(titleWithSpecialChars);
    }

    @Test
    void shouldCreateTitleWithNumbers_whenValidStringWithNumbersProvided() {
        // given
        String titleWithNumbers = "Java 25: New Features and Improvements";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(titleWithNumbers);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(titleWithNumbers);
    }

    @Test
    void shouldCreateTitleWithLongString_whenValidLongStringProvided() {
        // given
        String longTitle = "This is a very long title that contains many words and should still be valid because there might be academic papers or technical documents with very long descriptive titles that need to be supported by our system";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(longTitle);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(longTitle);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoTitlesHaveSameValue() {
        // given
        String title = "Hexagonal Architecture";
        Title title1 = Title.validateThenCreate(title).get();
        Title title2 = Title.validateThenCreate(title).get();

        // when
        int hashCode1 = title1.hashCode();
        int hashCode2 = title2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoTitlesHaveSameValue() {
        // given
        String title = "Hexagonal Architecture";
        Title title1 = Title.validateThenCreate(title).get();
        Title title2 = Title.validateThenCreate(title).get();

        // when & then
        assertThat(title1).isEqualTo(title2);
        assertThat(title1.equals(title2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoTitlesHaveDifferentValues() {
        // given
        Title title1 = Title.validateThenCreate("Clean Architecture").get();
        Title title2 = Title.validateThenCreate("Hexagonal Architecture").get();

        // when & then
        assertThat(title1).isNotEqualTo(title2);
        assertThat(title1.equals(title2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        Title title = Title.validateThenCreate("Clean Architecture").get();

        // when & then
        assertThat(title.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        Title title = Title.validateThenCreate("Clean Architecture").get();
        String stringValue = "Clean Architecture";

        // when & then
        assertThat(title.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnValue_whenValueMethodCalled() {
        // given
        String expectedValue = "Domain-Driven Design";
        Title title = Title.validateThenCreate(expectedValue).get();

        // when
        String actualValue = title.value();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldCreateTitleWithUnicodeCharacters_whenValidUnicodeStringProvided() {
        // given
        String unicodeTitle = "Архитектура программного обеспечения: 软件架构设计";

        // when
        Validation<Error, Title> result = Title.validateThenCreate(unicodeTitle);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(unicodeTitle);
    }
}
