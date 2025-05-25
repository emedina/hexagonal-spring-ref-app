package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for AuthorId.
 *
 * @author Enrique Medina Montenegro
 */
class AuthorIdTest {

    @Test
    void shouldCreateValidAuthorId_whenValidStringProvided() {
        // given
        String validId = "author-456";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(validId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(validId);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        String nullId = null;

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(nullId);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenEmptyIdProvided() {
        // given
        String emptyId = "";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(emptyId);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateAuthorIdWithUuid_whenValidUuidProvided() {
        // given
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(uuidId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(uuidId);
    }

    @Test
    void shouldCreateAuthorIdWithEmail_whenValidEmailProvided() {
        // given
        String emailId = "john.doe@example.com";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(emailId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(emailId);
    }

    @Test
    void shouldCreateAuthorIdWithNumericString_whenValidNumericStringProvided() {
        // given
        String numericId = "12345";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(numericId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(numericId);
    }

    @Test
    void shouldCreateAuthorIdWithSpecialCharacters_whenValidStringWithSpecialCharsProvided() {
        // given
        String idWithSpecialChars = "author_123-test@domain";

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(idWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(idWithSpecialChars);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoAuthorIdsHaveSameValue() {
        // given
        String id = "author-789";
        AuthorId authorId1 = AuthorId.validateThenCreate(id).get();
        AuthorId authorId2 = AuthorId.validateThenCreate(id).get();

        // when
        int hashCode1 = authorId1.hashCode();
        int hashCode2 = authorId2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoAuthorIdsHaveSameValue() {
        // given
        String id = "author-789";
        AuthorId authorId1 = AuthorId.validateThenCreate(id).get();
        AuthorId authorId2 = AuthorId.validateThenCreate(id).get();

        // when & then
        assertThat(authorId1).isEqualTo(authorId2);
        assertThat(authorId1.equals(authorId2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoAuthorIdsHaveDifferentValues() {
        // given
        AuthorId authorId1 = AuthorId.validateThenCreate("author-123").get();
        AuthorId authorId2 = AuthorId.validateThenCreate("author-456").get();

        // when & then
        assertThat(authorId1).isNotEqualTo(authorId2);
        assertThat(authorId1.equals(authorId2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        AuthorId authorId = AuthorId.validateThenCreate("author-123").get();

        // when & then
        assertThat(authorId.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        AuthorId authorId = AuthorId.validateThenCreate("author-123").get();
        String stringValue = "author-123";

        // when & then
        assertThat(authorId.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnValue_whenValueMethodCalled() {
        // given
        String expectedValue = "author-999";
        AuthorId authorId = AuthorId.validateThenCreate(expectedValue).get();

        // when
        String actualValue = authorId.value();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldCreateAuthorIdWithLongString_whenValidLongStringProvided() {
        // given
        String longId = "author-" + "a".repeat(500);

        // when
        Validation<Error, AuthorId> result = AuthorId.validateThenCreate(longId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(longId);
    }
}
