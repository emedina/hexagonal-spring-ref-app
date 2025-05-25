package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for Author.
 *
 * @author Enrique Medina Montenegro
 */
class AuthorTest {

    @Test
    void shouldCreateValidAuthor_whenValidIdAndNameProvided() {
        // given
        AuthorId validId = AuthorId.validateThenCreate("author-123").get();
        PersonName validName = PersonName.validateThenCreate("William Shakespeare").get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(validId, validName);

        // then
        assertThat(result.isValid()).isTrue();
        Author author = result.get();
        assertThat(author.id()).isEqualTo(validId);
        assertThat(author.name()).isEqualTo(validName);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        AuthorId nullId = null;
        PersonName validName = PersonName.validateThenCreate("John Doe").get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(nullId, validName);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnValidationError_whenNullNameProvided() {
        // given
        AuthorId validId = AuthorId.validateThenCreate("author-456").get();
        PersonName nullName = null;

        // when
        Validation<Error, Author> result = Author.validateThenCreate(validId, nullName);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnMultipleValidationErrors_whenBothIdAndNameAreNull() {
        // given
        AuthorId nullId = null;
        PersonName nullName = null;

        // when
        Validation<Error, Author> result = Author.validateThenCreate(nullId, nullName);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(2);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(1)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldCreateAuthorWithComplexName_whenValidComplexNameProvided() {
        // given
        AuthorId validId = AuthorId.validateThenCreate("author-789").get();
        PersonName complexName = PersonName.validateThenCreate("José María García-López de la Torre").get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(validId, complexName);

        // then
        assertThat(result.isValid()).isTrue();
        Author author = result.get();
        assertThat(author.id()).isEqualTo(validId);
        assertThat(author.name()).isEqualTo(complexName);
    }

    @Test
    void shouldCreateAuthorWithUuidId_whenValidUuidIdProvided() {
        // given
        AuthorId uuidId = AuthorId.validateThenCreate("550e8400-e29b-41d4-a716-446655440000").get();
        PersonName validName = PersonName.validateThenCreate("Jane Austen").get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(uuidId, validName);

        // then
        assertThat(result.isValid()).isTrue();
        Author author = result.get();
        assertThat(author.id()).isEqualTo(uuidId);
        assertThat(author.name()).isEqualTo(validName);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoAuthorsHaveSameId() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-same").get();
        PersonName name1 = PersonName.validateThenCreate("Name One").get();
        PersonName name2 = PersonName.validateThenCreate("Name Two").get();
        Author author1 = Author.validateThenCreate(id, name1).get();
        Author author2 = Author.validateThenCreate(id, name2).get();

        // when
        int hashCode1 = author1.hashCode();
        int hashCode2 = author2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoAuthorsHaveSameId() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-same").get();
        PersonName name1 = PersonName.validateThenCreate("Name One").get();
        PersonName name2 = PersonName.validateThenCreate("Name Two").get();
        Author author1 = Author.validateThenCreate(id, name1).get();
        Author author2 = Author.validateThenCreate(id, name2).get();

        // when & then
        assertThat(author1).isEqualTo(author2);
        assertThat(author1.equals(author2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoAuthorsHaveDifferentIds() {
        // given
        AuthorId id1 = AuthorId.validateThenCreate("author-1").get();
        AuthorId id2 = AuthorId.validateThenCreate("author-2").get();
        PersonName name = PersonName.validateThenCreate("Same Name").get();
        Author author1 = Author.validateThenCreate(id1, name).get();
        Author author2 = Author.validateThenCreate(id2, name).get();

        // when & then
        assertThat(author1).isNotEqualTo(author2);
        assertThat(author1.equals(author2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-123").get();
        PersonName name = PersonName.validateThenCreate("John Doe").get();
        Author author = Author.validateThenCreate(id, name).get();

        // when & then
        assertThat(author.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-123").get();
        PersonName name = PersonName.validateThenCreate("John Doe").get();
        Author author = Author.validateThenCreate(id, name).get();
        String stringValue = "author-123";

        // when & then
        assertThat(author.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        AuthorId expectedId = AuthorId.validateThenCreate("author-999").get();
        PersonName name = PersonName.validateThenCreate("Test Author").get();
        Author author = Author.validateThenCreate(expectedId, name).get();

        // when
        AuthorId actualId = author.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnName_whenNameMethodCalled() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-999").get();
        PersonName expectedName = PersonName.validateThenCreate("Test Author").get();
        Author author = Author.validateThenCreate(id, expectedName).get();

        // when
        PersonName actualName = author.name();

        // then
        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    void shouldCreateAuthorWithSingleWordName_whenValidSingleWordNameProvided() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-single").get();
        PersonName singleName = PersonName.validateThenCreate("Plato").get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(id, singleName);

        // then
        assertThat(result.isValid()).isTrue();
        Author author = result.get();
        assertThat(author.id()).isEqualTo(id);
        assertThat(author.name()).isEqualTo(singleName);
    }

    @Test
    void shouldCreateAuthorWithVeryLongName_whenValidLongNameProvided() {
        // given
        AuthorId id = AuthorId.validateThenCreate("author-long").get();
        PersonName longName = PersonName.validateThenCreate(
            "Pablo Diego José Francisco de Paula Juan Nepomuceno María de los Remedios Cipriano de la Santísima Trinidad Ruiz y Picasso"
        ).get();

        // when
        Validation<Error, Author> result = Author.validateThenCreate(id, longName);

        // then
        assertThat(result.isValid()).isTrue();
        Author author = result.get();
        assertThat(author.id()).isEqualTo(id);
        assertThat(author.name()).isEqualTo(longName);
    }
}
