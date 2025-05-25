package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for Article.
 *
 * @author Enrique Medina Montenegro
 */
class ArticleTest {

    @Test
    void shouldCreateValidArticle_whenAllValidFieldsProvided() {
        // given
        ArticleId validId = ArticleId.validateThenCreate("article-123").get();
        Title validTitle = Title.validateThenCreate("Introduction to Hexagonal Architecture").get();
        Content validContent = Content.validateThenCreate(
            "This article explains the principles of hexagonal architecture.").get();
        Author validAuthor = createValidAuthor();

        // when
        Validation<Error, Article> result = Article.validateThenCreate(validId, validTitle, validContent, validAuthor);

        // then
        assertThat(result.isValid()).isTrue();
        Article article = result.get();
        assertThat(article.id()).isEqualTo(validId);
        assertThat(article.title()).isEqualTo(validTitle);
        assertThat(article.content()).isEqualTo(validContent);
        assertThat(article.author()).isEqualTo(validAuthor);
    }

    @Test
    void shouldReturnValidationError_whenNullIdProvided() {
        // given
        ArticleId nullId = null;
        Title validTitle = Title.validateThenCreate("Valid Title").get();
        Content validContent = Content.validateThenCreate("Valid content").get();
        Author validAuthor = createValidAuthor();

        // when
        Validation<Error, Article> result = Article.validateThenCreate(nullId, validTitle, validContent, validAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnValidationError_whenNullTitleProvided() {
        // given
        ArticleId validId = ArticleId.validateThenCreate("article-456").get();
        Title nullTitle = null;
        Content validContent = Content.validateThenCreate("Valid content").get();
        Author validAuthor = createValidAuthor();

        // when
        Validation<Error, Article> result = Article.validateThenCreate(validId, nullTitle, validContent, validAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnValidationError_whenNullContentProvided() {
        // given
        ArticleId validId = ArticleId.validateThenCreate("article-789").get();
        Title validTitle = Title.validateThenCreate("Valid Title").get();
        Content nullContent = null;
        Author validAuthor = createValidAuthor();

        // when
        Validation<Error, Article> result = Article.validateThenCreate(validId, validTitle, nullContent, validAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnValidationError_whenNullAuthorProvided() {
        // given
        ArticleId validId = ArticleId.validateThenCreate("article-101").get();
        Title validTitle = Title.validateThenCreate("Valid Title").get();
        Content validContent = Content.validateThenCreate("Valid content").get();
        Author nullAuthor = null;

        // when
        Validation<Error, Article> result = Article.validateThenCreate(validId, validTitle, validContent, nullAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnMultipleValidationErrors_whenMultipleFieldsAreNull() {
        // given
        ArticleId nullId = null;
        Title nullTitle = null;
        Content validContent = Content.validateThenCreate("Valid content").get();
        Author nullAuthor = null;

        // when
        Validation<Error, Article> result = Article.validateThenCreate(nullId, nullTitle, validContent, nullAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(3);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(1)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(2)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldReturnAllValidationErrors_whenAllFieldsAreNull() {
        // given
        ArticleId nullId = null;
        Title nullTitle = null;
        Content nullContent = null;
        Author nullAuthor = null;

        // when
        Validation<Error, Article> result = Article.validateThenCreate(nullId, nullTitle, nullContent, nullAuthor);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(4);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(1)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(2)).isInstanceOf(ValidationError.CannotBeNull.class);
        assertThat(validationErrors.errors().get(3)).isInstanceOf(ValidationError.CannotBeNull.class);
    }

    @Test
    void shouldCreateArticleWithComplexContent_whenValidComplexContentProvided() {
        // given
        ArticleId validId = ArticleId.validateThenCreate("article-complex").get();
        Title validTitle = Title.validateThenCreate("Advanced Software Architecture Patterns").get();
        Content complexContent = Content.validateThenCreate(
            "# Introduction\n\n" +
                "This article discusses various architectural patterns including:\n\n" +
                "1. **Hexagonal Architecture** (Ports and Adapters)\n" +
                "2. **Clean Architecture**\n" +
                "3. **Onion Architecture**\n\n" +
                "## Code Example\n\n" +
                "```java\n" +
                "public class DomainService {\n" +
                "    private final Repository repository;\n" +
                "    \n" +
                "    public void processData(Data data) {\n" +
                "        // Business logic here\n" +
                "    }\n" +
                "}\n" +
                "```\n\n" +
                "For more information, visit: https://example.com"
        ).get();
        Author validAuthor = createValidAuthor();

        // when
        Validation<Error, Article> result = Article.validateThenCreate(validId, validTitle, complexContent,
            validAuthor);

        // then
        assertThat(result.isValid()).isTrue();
        Article article = result.get();
        assertThat(article.content()).isEqualTo(complexContent);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoArticlesHaveSameId() {
        // given
        ArticleId id = ArticleId.validateThenCreate("article-same").get();
        Title title1 = Title.validateThenCreate("Title One").get();
        Title title2 = Title.validateThenCreate("Title Two").get();
        Content content1 = Content.validateThenCreate("Content One").get();
        Content content2 = Content.validateThenCreate("Content Two").get();
        Author author1 = createValidAuthor();
        Author author2 = createValidAuthor();

        Article article1 = Article.validateThenCreate(id, title1, content1, author1).get();
        Article article2 = Article.validateThenCreate(id, title2, content2, author2).get();

        // when
        int hashCode1 = article1.hashCode();
        int hashCode2 = article2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoArticlesHaveSameId() {
        // given
        ArticleId id = ArticleId.validateThenCreate("article-same").get();
        Title title1 = Title.validateThenCreate("Title One").get();
        Title title2 = Title.validateThenCreate("Title Two").get();
        Content content1 = Content.validateThenCreate("Content One").get();
        Content content2 = Content.validateThenCreate("Content Two").get();
        Author author1 = createValidAuthor();
        Author author2 = createValidAuthor();

        Article article1 = Article.validateThenCreate(id, title1, content1, author1).get();
        Article article2 = Article.validateThenCreate(id, title2, content2, author2).get();

        // when & then
        assertThat(article1).isEqualTo(article2);
        assertThat(article1.equals(article2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoArticlesHaveDifferentIds() {
        // given
        ArticleId id1 = ArticleId.validateThenCreate("article-1").get();
        ArticleId id2 = ArticleId.validateThenCreate("article-2").get();
        Title title = Title.validateThenCreate("Same Title").get();
        Content content = Content.validateThenCreate("Same content").get();
        Author author = createValidAuthor();

        Article article1 = Article.validateThenCreate(id1, title, content, author).get();
        Article article2 = Article.validateThenCreate(id2, title, content, author).get();

        // when & then
        assertThat(article1).isNotEqualTo(article2);
        assertThat(article1.equals(article2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        Article article = createValidArticle();

        // when & then
        assertThat(article.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        Article article = createValidArticle();
        String stringValue = "article-123";

        // when & then
        assertThat(article.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnTrue_whenEnforceEligibilityForPublicationCalled() {
        // given
        Article article = createValidArticle();

        // when
        Boolean result = article.enforceEligibilityForPublication();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnId_whenIdMethodCalled() {
        // given
        ArticleId expectedId = ArticleId.validateThenCreate("article-test-id").get();
        Title title = Title.validateThenCreate("Test Title").get();
        Content content = Content.validateThenCreate("Test content").get();
        Author author = createValidAuthor();
        Article article = Article.validateThenCreate(expectedId, title, content, author).get();

        // when
        ArticleId actualId = article.id();

        // then
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnTitle_whenTitleMethodCalled() {
        // given
        ArticleId id = ArticleId.validateThenCreate("article-test").get();
        Title expectedTitle = Title.validateThenCreate("Expected Title").get();
        Content content = Content.validateThenCreate("Test content").get();
        Author author = createValidAuthor();
        Article article = Article.validateThenCreate(id, expectedTitle, content, author).get();

        // when
        Title actualTitle = article.title();

        // then
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @Test
    void shouldReturnContent_whenContentMethodCalled() {
        // given
        ArticleId id = ArticleId.validateThenCreate("article-test").get();
        Title title = Title.validateThenCreate("Test Title").get();
        Content expectedContent = Content.validateThenCreate("Expected content for testing").get();
        Author author = createValidAuthor();
        Article article = Article.validateThenCreate(id, title, expectedContent, author).get();

        // when
        Content actualContent = article.content();

        // then
        assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void shouldReturnAuthor_whenAuthorMethodCalled() {
        // given
        ArticleId id = ArticleId.validateThenCreate("article-test").get();
        Title title = Title.validateThenCreate("Test Title").get();
        Content content = Content.validateThenCreate("Test content").get();
        Author expectedAuthor = createValidAuthor();
        Article article = Article.validateThenCreate(id, title, content, expectedAuthor).get();

        // when
        Author actualAuthor = article.author();

        // then
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    private Author createValidAuthor() {
        AuthorId authorId = AuthorId.validateThenCreate("author-123").get();
        PersonName authorName = PersonName.validateThenCreate("John Doe").get();
        return Author.validateThenCreate(authorId, authorName).get();
    }

    private Article createValidArticle() {
        ArticleId id = ArticleId.validateThenCreate("article-123").get();
        Title title = Title.validateThenCreate("Test Article").get();
        Content content = Content.validateThenCreate("This is test content").get();
        Author author = createValidAuthor();
        return Article.validateThenCreate(id, title, content, author).get();
    }
}
