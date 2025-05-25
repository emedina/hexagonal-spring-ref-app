package com.emedina.hexagonal.ref.app.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for CreateArticleCommand.
 *
 * @author Enrique Medina Montenegro
 */
class CreateArticleCommandTest {

    @Test
    void shouldCreateValidCommand_whenAllValidFieldsProvided() {
        // given
        String validId = "article-123";
        String validAuthorId = "author-456";
        String validTitle = "Introduction to Hexagonal Architecture";
        String validContent = "This article explains the principles of hexagonal architecture.";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, validAuthorId, validTitle, validContent);

        // then
        assertThat(result.isValid()).isTrue();
        CreateArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(validId);
        assertThat(command.authorId()).isEqualTo(validAuthorId);
        assertThat(command.title()).isEqualTo(validTitle);
        assertThat(command.content()).isEqualTo(validContent);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        String nullId = null;
        String validAuthorId = "author-456";
        String validTitle = "Valid Title";
        String validContent = "Valid content";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            nullId, validAuthorId, validTitle, validContent);

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
        String validAuthorId = "author-456";
        String validTitle = "Valid Title";
        String validContent = "Valid content";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            emptyId, validAuthorId, validTitle, validContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenNullAuthorIdProvided() {
        // given
        String validId = "article-123";
        String nullAuthorId = null;
        String validTitle = "Valid Title";
        String validContent = "Valid content";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, nullAuthorId, validTitle, validContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenNullTitleProvided() {
        // given
        String validId = "article-123";
        String validAuthorId = "author-456";
        String nullTitle = null;
        String validContent = "Valid content";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, validAuthorId, nullTitle, validContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenNullContentProvided() {
        // given
        String validId = "article-123";
        String validAuthorId = "author-456";
        String validTitle = "Valid Title";
        String nullContent = null;

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, validAuthorId, validTitle, nullContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnMultipleValidationErrors_whenMultipleFieldsAreInvalid() {
        // given
        String nullId = null;
        String emptyAuthorId = "";
        String validTitle = "Valid Title";
        String nullContent = null;

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            nullId, emptyAuthorId, validTitle, nullContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(3);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
        assertThat(validationErrors.errors().get(1)).isInstanceOf(ValidationError.Invalid.class);
        assertThat(validationErrors.errors().get(2)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnAllValidationErrors_whenAllFieldsAreInvalid() {
        // given
        String nullId = null;
        String emptyAuthorId = "";
        String nullTitle = null;
        String emptyContent = "";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            nullId, emptyAuthorId, nullTitle, emptyContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(4);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
        assertThat(validationErrors.errors().get(1)).isInstanceOf(ValidationError.Invalid.class);
        assertThat(validationErrors.errors().get(2)).isInstanceOf(ValidationError.Invalid.class);
        assertThat(validationErrors.errors().get(3)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateCommandWithComplexContent_whenValidComplexContentProvided() {
        // given
        String validId = "article-complex";
        String validAuthorId = "author-789";
        String validTitle = "Advanced Software Architecture Patterns";
        String complexContent = "# Introduction\n\n" +
            "This article discusses various architectural patterns including:\n\n" +
            "1. **Hexagonal Architecture** (Ports and Adapters)\n" +
            "2. **Clean Architecture**\n" +
            "3. **Onion Architecture**\n\n" +
            "## Code Example\n\n" +
            "```java\n" +
            "public class DomainService {\n" +
            "    private final Repository repository;\n" +
            "}\n" +
            "```";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, validAuthorId, validTitle, complexContent);

        // then
        assertThat(result.isValid()).isTrue();
        CreateArticleCommand command = result.get();
        assertThat(command.content()).isEqualTo(complexContent);
    }

    @Test
    void shouldCreateCommandWithSpecialCharacters_whenValidSpecialCharsProvided() {
        // given
        String idWithSpecialChars = "article-123_test@domain.com";
        String authorIdWithSpecialChars = "author_456-test@example.org";
        String titleWithSpecialChars = "Clean Architecture: A Craftsman's Guide to Software Structure & Design";
        String contentWithSpecialChars = "Content with special chars: @#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            idWithSpecialChars, authorIdWithSpecialChars, titleWithSpecialChars, contentWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        CreateArticleCommand command = result.get();
        assertThat(command.id()).isEqualTo(idWithSpecialChars);
        assertThat(command.authorId()).isEqualTo(authorIdWithSpecialChars);
        assertThat(command.title()).isEqualTo(titleWithSpecialChars);
        assertThat(command.content()).isEqualTo(contentWithSpecialChars);
    }

    @Test
    void shouldCreateCommandWithUnicodeCharacters_whenValidUnicodeProvided() {
        // given
        String validId = "article-unicode";
        String validAuthorId = "author-unicode";
        String unicodeTitle = "Архитектура программного обеспечения: 软件架构设计";
        String unicodeContent = "Content with unicode: 你好世界 🌍 Здравствуй мир";

        // when
        Validation<Error, CreateArticleCommand> result = CreateArticleCommand.validateThenCreate(
            validId, validAuthorId, unicodeTitle, unicodeContent);

        // then
        assertThat(result.isValid()).isTrue();
        CreateArticleCommand command = result.get();
        assertThat(command.title()).isEqualTo(unicodeTitle);
        assertThat(command.content()).isEqualTo(unicodeContent);
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        String expectedId = "article-test-id";
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            expectedId, "author-123", "Test Title", "Test content").get();

        // when
        String actualId = command.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnAuthorId_whenAuthorIdMethodCalled() {
        // given
        String expectedAuthorId = "author-test-id";
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", expectedAuthorId, "Test Title", "Test content").get();

        // when
        String actualAuthorId = command.authorId();

        // then
        assertThat(actualAuthorId).isEqualTo(expectedAuthorId);
    }

    @Test
    void shouldReturnTitle_whenTitleMethodCalled() {
        // given
        String expectedTitle = "Expected Test Title";
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", "author-456", expectedTitle, "Test content").get();

        // when
        String actualTitle = command.title();

        // then
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @Test
    void shouldReturnContent_whenContentMethodCalled() {
        // given
        String expectedContent = "Expected test content for validation";
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Test Title", expectedContent).get();

        // when
        String actualContent = command.content();

        // then
        assertThat(actualContent).isEqualTo(expectedContent);
    }
}
