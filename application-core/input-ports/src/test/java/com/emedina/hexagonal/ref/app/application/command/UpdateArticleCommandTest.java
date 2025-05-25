package com.emedina.hexagonal.ref.app.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for UpdateArticleCommand.
 *
 * @author Enrique Medina Montenegro
 */
class UpdateArticleCommandTest {

    @Test
    void shouldCreateValidCommand_whenAllValidFieldsProvided() {
        // given
        String validId = "article-123";
        String validAuthorId = "author-456";
        String validTitle = "Updated Introduction to Hexagonal Architecture";
        String validContent = "This updated article explains the principles of hexagonal architecture.";

        // when
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
            validId, validAuthorId, validTitle, validContent);

        // then
        assertThat(result.isValid()).isTrue();
        UpdateArticleCommand command = result.get();
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
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
    void shouldCreateCommandWithUpdatedContent_whenValidUpdatedContentProvided() {
        // given
        String validId = "article-update";
        String validAuthorId = "author-789";
        String updatedTitle = "Updated: Advanced Software Architecture Patterns";
        String updatedContent = "# Updated Introduction\n\n" +
            "This updated article now includes:\n\n" +
            "1. **Hexagonal Architecture** (Ports and Adapters) - Enhanced\n" +
            "2. **Clean Architecture** - New examples\n" +
            "3. **Onion Architecture** - Updated patterns\n\n" +
            "## Updated Code Example\n\n" +
            "```java\n" +
            "public class UpdatedDomainService {\n" +
            "    private final Repository repository;\n" +
            "    private final EventPublisher eventPublisher;\n" +
            "}\n" +
            "```";

        // when
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
            validId, validAuthorId, updatedTitle, updatedContent);

        // then
        assertThat(result.isValid()).isTrue();
        UpdateArticleCommand command = result.get();
        assertThat(command.title()).isEqualTo(updatedTitle);
        assertThat(command.content()).isEqualTo(updatedContent);
    }

    @Test
    void shouldCreateCommandWithSameContentAsOriginal_whenNoContentChanges() {
        // given
        String validId = "article-same";
        String validAuthorId = "author-same";
        String sameTitle = "Unchanged Title";
        String sameContent = "This content remains the same after update.";

        // when
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
            validId, validAuthorId, sameTitle, sameContent);

        // then
        assertThat(result.isValid()).isTrue();
        UpdateArticleCommand command = result.get();
        assertThat(command.title()).isEqualTo(sameTitle);
        assertThat(command.content()).isEqualTo(sameContent);
    }

    @Test
    void shouldCreateCommandWithMinorChanges_whenSmallUpdatesProvided() {
        // given
        String validId = "article-minor";
        String validAuthorId = "author-minor";
        String minorTitle = "Introduction to Hexagonal Architecture (v2)";
        String minorContent = "This article explains the principles of hexagonal architecture. Updated with new examples.";

        // when
        Validation<Error, UpdateArticleCommand> result = UpdateArticleCommand.validateThenCreate(
            validId, validAuthorId, minorTitle, minorContent);

        // then
        assertThat(result.isValid()).isTrue();
        UpdateArticleCommand command = result.get();
        assertThat(command.title()).contains("v2");
        assertThat(command.content()).contains("Updated with new examples");
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        String expectedId = "article-update-test";
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            expectedId, "author-123", "Test Title", "Test content").get();

        // when
        String actualId = command.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnAuthorId_whenAuthorIdMethodCalled() {
        // given
        String expectedAuthorId = "author-update-test";
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", expectedAuthorId, "Test Title", "Test content").get();

        // when
        String actualAuthorId = command.authorId();

        // then
        assertThat(actualAuthorId).isEqualTo(expectedAuthorId);
    }

    @Test
    void shouldReturnTitle_whenTitleMethodCalled() {
        // given
        String expectedTitle = "Expected Updated Title";
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", "author-456", expectedTitle, "Test content").get();

        // when
        String actualTitle = command.title();

        // then
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @Test
    void shouldReturnContent_whenContentMethodCalled() {
        // given
        String expectedContent = "Expected updated content for validation";
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Test Title", expectedContent).get();

        // when
        String actualContent = command.content();

        // then
        assertThat(actualContent).isEqualTo(expectedContent);
    }
}
