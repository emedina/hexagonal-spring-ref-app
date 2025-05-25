package com.emedina.hexagonal.ref.app.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for DeleteArticleCommand.
 *
 * @author Enrique Medina Montenegro
 */
class DeleteArticleCommandTest {

    @Test
    void shouldCreateValidCommand_whenValidIdProvided() {
        // given
        String validId = "article-123";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(validId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(validId);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        String nullId = null;

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(nullId);

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
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(emptyId);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateCommandWithUuidId_whenValidUuidProvided() {
        // given
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(uuidId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(uuidId);
    }

    @Test
    void shouldCreateCommandWithSpecialCharacters_whenValidSpecialCharsProvided() {
        // given
        String idWithSpecialChars = "article-123_test@domain.com";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(idWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(idWithSpecialChars);
    }

    @Test
    void shouldCreateCommandWithNumericId_whenValidNumericStringProvided() {
        // given
        String numericId = "12345";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(numericId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(numericId);
    }

    @Test
    void shouldCreateCommandWithLongId_whenValidLongStringProvided() {
        // given
        String longId = "article-" + "a".repeat(500);

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(longId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(longId);
    }

    @Test
    void shouldCreateCommandWithUnicodeCharacters_whenValidUnicodeProvided() {
        // given
        String unicodeId = "article-测试-тест-🌍";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(unicodeId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(unicodeId);
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        String expectedId = "article-test-delete";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(expectedId).get();

        // when
        String actualId = command.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldCreateCommandWithWhitespaceId_whenValidWhitespaceProvided() {
        // given
        String whitespaceId = "   article-123   ";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(whitespaceId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(whitespaceId);
    }

    @Test
    void shouldCreateCommandWithUrlLikeId_whenValidUrlLikeStringProvided() {
        // given
        String urlLikeId = "https://example.com/articles/123";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(urlLikeId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(urlLikeId);
    }

    @Test
    void shouldCreateCommandWithJsonLikeId_whenValidJsonLikeStringProvided() {
        // given
        String jsonLikeId = "{\"articleId\":\"123\",\"version\":\"v1\"}";

        // when
        Validation<Error, DeleteArticleCommand> result = DeleteArticleCommand.validateThenCreate(jsonLikeId);

        // then
        assertThat(result.isValid()).isTrue();
        DeleteArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(jsonLikeId);
    }
}
