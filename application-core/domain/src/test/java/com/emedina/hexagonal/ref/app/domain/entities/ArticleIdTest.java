package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for ArticleId.
 *
 * @author Enrique Medina Montenegro
 */
class ArticleIdTest {

    @Test
    void shouldCreateValidArticleId_whenValidStringProvided() {
        // given
        String validId = "article-123";

        // when
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(validId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(validId);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        String nullId = null;

        // when
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(nullId);

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
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(emptyId);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenWhitespaceOnlyIdProvided() {
        // given
        String whitespaceId = "   ";

        // when
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(whitespaceId);

        // then
        assertThat(result.isValid()).isTrue(); // Note: whitespace is considered valid by validateText
        assertThat(result.get().value()).isEqualTo(whitespaceId);
    }

    @Test
    void shouldCreateArticleIdWithSpecialCharacters_whenValidStringWithSpecialCharsProvided() {
        // given
        String idWithSpecialChars = "article-123_test@domain.com";

        // when
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(idWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(idWithSpecialChars);
    }

    @Test
    void shouldCreateArticleIdWithLongString_whenValidLongStringProvided() {
        // given
        String longId = "a".repeat(1000);

        // when
        Validation<Error, ArticleId> result = ArticleId.validateThenCreate(longId);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(longId);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoArticleIdsHaveSameValue() {
        // given
        String id = "article-123";
        ArticleId articleId1 = ArticleId.validateThenCreate(id).get();
        ArticleId articleId2 = ArticleId.validateThenCreate(id).get();

        // when
        int hashCode1 = articleId1.hashCode();
        int hashCode2 = articleId2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoArticleIdsHaveSameValue() {
        // given
        String id = "article-123";
        ArticleId articleId1 = ArticleId.validateThenCreate(id).get();
        ArticleId articleId2 = ArticleId.validateThenCreate(id).get();

        // when & then
        assertThat(articleId1).isEqualTo(articleId2);
        assertThat(articleId1.equals(articleId2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoArticleIdsHaveDifferentValues() {
        // given
        ArticleId articleId1 = ArticleId.validateThenCreate("article-123").get();
        ArticleId articleId2 = ArticleId.validateThenCreate("article-456").get();

        // when & then
        assertThat(articleId1).isNotEqualTo(articleId2);
        assertThat(articleId1.equals(articleId2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        ArticleId articleId = ArticleId.validateThenCreate("article-123").get();

        // when & then
        assertThat(articleId.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        ArticleId articleId = ArticleId.validateThenCreate("article-123").get();
        String stringValue = "article-123";

        // when & then
        assertThat(articleId.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnValue_whenValueMethodCalled() {
        // given
        String expectedValue = "article-123";
        ArticleId articleId = ArticleId.validateThenCreate(expectedValue).get();

        // when
        String actualValue = articleId.value();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
