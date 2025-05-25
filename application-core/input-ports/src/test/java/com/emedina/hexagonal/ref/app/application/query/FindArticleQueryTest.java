package com.emedina.hexagonal.ref.app.application.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for FindArticleQuery.
 *
 * @author Enrique Medina Montenegro
 */
class FindArticleQueryTest {

    @Test
    void shouldCreateValidQuery_whenValidIdProvided() {
        // given
        String validId = "article-123";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(validId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(validId);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        String nullId = null;

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(nullId);

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
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(emptyId);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateQueryWithUuidId_whenValidUuidProvided() {
        // given
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(uuidId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(uuidId);
    }

    @Test
    void shouldCreateQueryWithSpecialCharacters_whenValidSpecialCharsProvided() {
        // given
        String idWithSpecialChars = "article-123_test@domain.com";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(idWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(idWithSpecialChars);
    }

    @Test
    void shouldCreateQueryWithNumericId_whenValidNumericStringProvided() {
        // given
        String numericId = "12345";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(numericId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(numericId);
    }

    @Test
    void shouldCreateQueryWithLongId_whenValidLongStringProvided() {
        // given
        String longId = "article-" + "a".repeat(500);

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(longId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(longId);
    }

    @Test
    void shouldCreateQueryWithUnicodeCharacters_whenValidUnicodeProvided() {
        // given
        String unicodeId = "article-测试-тест-🌍";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(unicodeId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(unicodeId);
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        String expectedId = "article-find-test";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(expectedId).get();

        // when
        String actualId = query.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldCreateQueryWithWhitespaceId_whenValidWhitespaceProvided() {
        // given
        String whitespaceId = "   article-123   ";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(whitespaceId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(whitespaceId);
    }

    @Test
    void shouldCreateQueryWithUrlLikeId_whenValidUrlLikeStringProvided() {
        // given
        String urlLikeId = "https://example.com/articles/123";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(urlLikeId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(urlLikeId);
    }

    @Test
    void shouldCreateQueryWithJsonLikeId_whenValidJsonLikeStringProvided() {
        // given
        String jsonLikeId = "{\"articleId\":\"123\",\"version\":\"v1\"}";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(jsonLikeId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(jsonLikeId);
    }

    @Test
    void shouldCreateQueryWithSlugId_whenValidSlugProvided() {
        // given
        String slugId = "introduction-to-hexagonal-architecture";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(slugId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(slugId);
    }

    @Test
    void shouldCreateQueryWithVersionedId_whenValidVersionedIdProvided() {
        // given
        String versionedId = "article-123-v2.1.0";

        // when
        Validation<Error, FindArticleQuery> result = FindArticleQuery.validateThenCreate(versionedId);

        // then
        assertThat(result.isValid()).isTrue();
        FindArticleQuery query = result.get();
        assertThat(query.id()).isEqualTo(versionedId);
    }
}
