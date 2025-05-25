package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for Content.
 *
 * @author Enrique Medina Montenegro
 */
class ContentTest {

    @Test
    void shouldCreateValidContent_whenValidStringProvided() {
        // given
        String validContent = "This is the main content of the article discussing hexagonal architecture principles.";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(validContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(validContent);
    }

    @Test
    void shouldReturnValidationError_whenNullContentProvided() {
        // given
        String nullContent = null;

        // when
        Validation<Error, Content> result = Content.validateThenCreate(nullContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenEmptyContentProvided() {
        // given
        String emptyContent = "";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(emptyContent);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreateContentWithMultipleLines_whenValidMultiLineStringProvided() {
        // given
        String multiLineContent = "Line 1: Introduction to the topic.\n" +
            "Line 2: Detailed explanation of concepts.\n" +
            "Line 3: Conclusion and final thoughts.";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(multiLineContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(multiLineContent);
    }

    @Test
    void shouldCreateContentWithSpecialCharacters_whenValidStringWithSpecialCharsProvided() {
        // given
        String contentWithSpecialChars = "Content with special chars: @#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(contentWithSpecialChars);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(contentWithSpecialChars);
    }

    @Test
    void shouldCreateContentWithHtmlTags_whenValidStringWithHtmlProvided() {
        // given
        String htmlContent = "<p>This is a paragraph with <strong>bold</strong> and <em>italic</em> text.</p>";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(htmlContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(htmlContent);
    }

    @Test
    void shouldCreateContentWithVeryLongText_whenValidLongStringProvided() {
        // given
        String longContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(100);

        // when
        Validation<Error, Content> result = Content.validateThenCreate(longContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(longContent);
    }

    @Test
    void shouldCreateContentWithCodeSnippets_whenValidStringWithCodeProvided() {
        // given
        String codeContent = "Here's a Java example:\n" +
            "```java\n" +
            "public class Example {\n" +
            "    public void method() {\n" +
            "        System.out.println(\"Hello World\");\n" +
            "    }\n" +
            "}\n" +
            "```";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(codeContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(codeContent);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoContentsHaveSameValue() {
        // given
        String content = "Sample article content";
        Content content1 = Content.validateThenCreate(content).get();
        Content content2 = Content.validateThenCreate(content).get();

        // when
        int hashCode1 = content1.hashCode();
        int hashCode2 = content2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoContentsHaveSameValue() {
        // given
        String content = "Sample article content";
        Content content1 = Content.validateThenCreate(content).get();
        Content content2 = Content.validateThenCreate(content).get();

        // when & then
        assertThat(content1).isEqualTo(content2);
        assertThat(content1.equals(content2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoContentsHaveDifferentValues() {
        // given
        Content content1 = Content.validateThenCreate("First content").get();
        Content content2 = Content.validateThenCreate("Second content").get();

        // when & then
        assertThat(content1).isNotEqualTo(content2);
        assertThat(content1.equals(content2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        Content content = Content.validateThenCreate("Sample content").get();

        // when & then
        assertThat(content.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        Content content = Content.validateThenCreate("Sample content").get();
        String stringValue = "Sample content";

        // when & then
        assertThat(content.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnValue_whenValueMethodCalled() {
        // given
        String expectedValue = "Expected content value";
        Content content = Content.validateThenCreate(expectedValue).get();

        // when
        String actualValue = content.value();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldCreateContentWithUnicodeCharacters_whenValidUnicodeStringProvided() {
        // given
        String unicodeContent = "Content with unicode: 你好世界 🌍 Здравствуй мир";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(unicodeContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(unicodeContent);
    }

    @Test
    void shouldCreateContentWithJsonData_whenValidJsonStringProvided() {
        // given
        String jsonContent = "{\"title\": \"Sample Article\", \"author\": \"John Doe\", \"tags\": [\"tech\", \"architecture\"]}";

        // when
        Validation<Error, Content> result = Content.validateThenCreate(jsonContent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(jsonContent);
    }
}
