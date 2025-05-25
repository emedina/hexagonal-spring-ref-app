package com.emedina.hexagonal.ref.app.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.entities.Author;
import com.emedina.hexagonal.ref.app.domain.entities.AuthorId;
import com.emedina.hexagonal.ref.app.domain.entities.Content;
import com.emedina.hexagonal.ref.app.domain.entities.PersonName;
import com.emedina.hexagonal.ref.app.domain.entities.Title;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Validation;

/**
 * Unit tests for ArticleMapper.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ArticleMapper Tests")
class ArticleMapperTest {

    private final ArticleMapper mapper = ArticleMapper.INSTANCE;

    @Nested
    @DisplayName("Given toArticleDto method")
    class ToArticleDtoTests {

        @Test
        @DisplayName("When valid article provided, then should map to ArticleDTO")
        void shouldMapToArticleDto_whenValidArticleProvided() {
            // Given
            var articleId = ArticleId.validateThenCreate("valid-id").get();
            var title = Title.validateThenCreate("Valid Title").get();
            var content = Content.validateThenCreate("Valid content for the article").get();
            var authorId = AuthorId.validateThenCreate("author-123").get();
            var personName = PersonName.validateThenCreate("John Doe").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var article = Article.validateThenCreate(articleId, title, content, author).get();

            // When
            ArticleDTO result = mapper.toArticleDto(article);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo("valid-id");
            assertThat(result.title()).isEqualTo("Valid Title");
            assertThat(result.content()).isEqualTo("Valid content for the article");
            assertThat(result.author()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("When article with unicode content provided, then should map correctly")
        void shouldMapToArticleDto_whenUnicodeContentProvided() {
            // Given
            var articleId = ArticleId.validateThenCreate("unicode-id").get();
            var title = Title.validateThenCreate("Título con Ñ y Ç").get();
            var content = Content.validateThenCreate("Contenido con caracteres especiales: áéíóú, 中文, 🚀").get();
            var authorId = AuthorId.validateThenCreate("author-unicode").get();
            var personName = PersonName.validateThenCreate("José María Azañón").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var article = Article.validateThenCreate(articleId, title, content, author).get();

            // When
            ArticleDTO result = mapper.toArticleDto(article);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo("unicode-id");
            assertThat(result.title()).isEqualTo("Título con Ñ y Ç");
            assertThat(result.content()).isEqualTo("Contenido con caracteres especiales: áéíóú, 中文, 🚀");
            assertThat(result.author()).isEqualTo("José María Azañón");
        }

        @Test
        @DisplayName("When article with long content provided, then should map correctly")
        void shouldMapToArticleDto_whenLongContentProvided() {
            // Given
            var longContent = "A".repeat(1000);
            var articleId = ArticleId.validateThenCreate("long-content-id").get();
            var title = Title.validateThenCreate("Long Content Article").get();
            var content = Content.validateThenCreate(longContent).get();
            var authorId = AuthorId.validateThenCreate("author-long").get();
            var personName = PersonName.validateThenCreate("Long Content Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var article = Article.validateThenCreate(articleId, title, content, author).get();

            // When
            ArticleDTO result = mapper.toArticleDto(article);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(1000);
            assertThat(result.content()).isEqualTo(longContent);
        }
    }

    @Nested
    @DisplayName("Given toAuthor method")
    class ToAuthorTests {

        @Test
        @DisplayName("When valid AuthorDTO provided, then should create valid Author")
        void shouldCreateValidAuthor_whenValidAuthorDTOProvided() {
            // Given
            var authorDTO = new AuthorDTO("author-123", "John Doe");

            // When
            Validation<Error, Author> result = mapper.toAuthor(authorDTO);

            // Then
            assertThat(result.isValid()).isTrue();
            var author = result.get();
            assertThat(author.id().value()).isEqualTo("author-123");
            assertThat(author.name().value()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("When AuthorDTO with invalid id provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidIdProvided() {
            // Given
            var authorDTO = new AuthorDTO("", "John Doe");

            // When
            Validation<Error, Author> result = mapper.toAuthor(authorDTO);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When AuthorDTO with invalid name provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidNameProvided() {
            // Given
            var authorDTO = new AuthorDTO("author-123", "");

            // When
            Validation<Error, Author> result = mapper.toAuthor(authorDTO);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When AuthorDTO with null values provided, then should return validation error")
        void shouldReturnValidationError_whenNullValuesProvided() {
            // Given
            var authorDTO = new AuthorDTO(null, null);

            // When
            Validation<Error, Author> result = mapper.toAuthor(authorDTO);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When AuthorDTO with unicode name provided, then should create valid Author")
        void shouldCreateValidAuthor_whenUnicodeNameProvided() {
            // Given
            var authorDTO = new AuthorDTO("unicode-author", "José María Ñoño");

            // When
            Validation<Error, Author> result = mapper.toAuthor(authorDTO);

            // Then
            assertThat(result.isValid()).isTrue();
            var author = result.get();
            assertThat(author.name().value()).isEqualTo("José María Ñoño");
        }
    }

    @Nested
    @DisplayName("Given toArticle with CreateArticleCommand method")
    class ToArticleWithCreateCommandTests {

        @Test
        @DisplayName("When valid CreateArticleCommand and AuthorDTO provided, then should create valid Article")
        void shouldCreateValidArticle_whenValidCommandAndAuthorProvided() {
            // Given
            var command = CreateArticleCommand.validateThenCreate("article-123", "author-123", "Test Title",
                "Test content").get();
            var authorDTO = new AuthorDTO("author-123", "John Doe");

            // When
            Validation<Error, Article> result = mapper.toArticle(command, authorDTO);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.id().value()).isEqualTo("article-123");
            assertThat(article.title().value()).isEqualTo("Test Title");
            assertThat(article.content().value()).isEqualTo("Test content");
            assertThat(article.author().name().value()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("When CreateArticleCommand with invalid data provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidCommandProvided() {
            // Given
            var validCommand = CreateArticleCommand.validateThenCreate("valid-id", "Test Title", "Test content",
                "author-123").get();
            var authorDTO = new AuthorDTO("author-123", "John Doe");

            // When - Test with empty id through direct method call
            Validation<Error, Article> result = mapper.toArticle("", "Test Title", "Test content",
                Author.validateThenCreate(AuthorId.validateThenCreate("author-123").get(),
                    PersonName.validateThenCreate("John Doe").get()).get());

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When valid CreateArticleCommand but invalid AuthorDTO provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidAuthorProvided() {
            // Given
            var command = CreateArticleCommand.validateThenCreate("article-123", "author-123", "Test Title",
                "Test content").get();
            var authorDTO = new AuthorDTO("", "John Doe");

            // When
            Validation<Error, Article> result = mapper.toArticle(command, authorDTO);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When CreateArticleCommand with unicode content provided, then should create valid Article")
        void shouldCreateValidArticle_whenUnicodeContentProvided() {
            // Given
            var command = CreateArticleCommand.validateThenCreate("unicode-article", "unicode-author",
                "Título Español", "Contenido con ñ y ç").get();
            var authorDTO = new AuthorDTO("unicode-author", "José María");

            // When
            Validation<Error, Article> result = mapper.toArticle(command, authorDTO);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.title().value()).isEqualTo("Título Español");
            assertThat(article.content().value()).isEqualTo("Contenido con ñ y ç");
            assertThat(article.author().name().value()).isEqualTo("José María");
        }
    }

    @Nested
    @DisplayName("Given toArticle with UpdateArticleCommand method")
    class ToArticleWithUpdateCommandTests {

        @Test
        @DisplayName("When valid UpdateArticleCommand and AuthorDTO provided, then should create valid Article")
        void shouldCreateValidArticle_whenValidCommandAndAuthorProvided() {
            // Given
            var command = UpdateArticleCommand.validateThenCreate("article-456", "author-456", "Updated Title",
                "Updated content").get();
            var authorDTO = new AuthorDTO("author-456", "Jane Smith");

            // When
            Validation<Error, Article> result = mapper.toArticle(command, authorDTO);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.id().value()).isEqualTo("article-456");
            assertThat(article.title().value()).isEqualTo("Updated Title");
            assertThat(article.content().value()).isEqualTo("Updated content");
            assertThat(article.author().name().value()).isEqualTo("Jane Smith");
        }

        @Test
        @DisplayName("When UpdateArticleCommand with invalid data provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidCommandProvided() {
            // Given - Test with empty title through direct method call
            var author = Author.validateThenCreate(AuthorId.validateThenCreate("author-456").get(),
                PersonName.validateThenCreate("Jane Smith").get()).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("article-456", "", "Updated content", author);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When valid UpdateArticleCommand but invalid AuthorDTO provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidAuthorProvided() {
            // Given
            var command = UpdateArticleCommand.validateThenCreate("article-456", "Updated Title", "Updated content",
                "author-456").get();
            var authorDTO = new AuthorDTO("author-456", "");

            // When
            Validation<Error, Article> result = mapper.toArticle(command, authorDTO);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }
    }

    @Nested
    @DisplayName("Given toArticle with individual parameters method")
    class ToArticleWithParametersTests {

        @Test
        @DisplayName("When valid parameters provided, then should create valid Article")
        void shouldCreateValidArticle_whenValidParametersProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("param-author").get();
            var personName = PersonName.validateThenCreate("Parameter Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("param-article", "Parameter Title",
                "Parameter content", author);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.id().value()).isEqualTo("param-article");
            assertThat(article.title().value()).isEqualTo("Parameter Title");
            assertThat(article.content().value()).isEqualTo("Parameter content");
            assertThat(article.author().name().value()).isEqualTo("Parameter Author");
        }

        @Test
        @DisplayName("When invalid id provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidIdProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("param-author").get();
            var personName = PersonName.validateThenCreate("Parameter Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("", "Parameter Title", "Parameter content", author);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When invalid title provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidTitleProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("param-author").get();
            var personName = PersonName.validateThenCreate("Parameter Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("param-article", "", "Parameter content", author);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When invalid content provided, then should return validation error")
        void shouldReturnValidationError_whenInvalidContentProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("param-author").get();
            var personName = PersonName.validateThenCreate("Parameter Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("param-article", "Parameter Title", "", author);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When multiple invalid parameters provided, then should return validation error")
        void shouldReturnValidationError_whenMultipleInvalidParametersProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("param-author").get();
            var personName = PersonName.validateThenCreate("Parameter Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("", "", "", author);

            // Then
            assertThat(result.isInvalid()).isTrue();
            assertThat(result.getError()).isInstanceOf(Error.MultipleErrors.class);
        }

        @Test
        @DisplayName("When parameters with special characters provided, then should create valid Article")
        void shouldCreateValidArticle_whenSpecialCharactersProvided() {
            // Given
            var authorId = AuthorId.validateThenCreate("special-author-123").get();
            var personName = PersonName.validateThenCreate("Author with Special Chars @#$").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("special-article-456", "Title with @#$ chars",
                "Content with special chars: !@#$%^&*()", author);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.title().value()).isEqualTo("Title with @#$ chars");
            assertThat(article.content().value()).isEqualTo("Content with special chars: !@#$%^&*()");
        }

        @Test
        @DisplayName("When parameters with very long content provided, then should create valid Article")
        void shouldCreateValidArticle_whenVeryLongContentProvided() {
            // Given
            var longContent = "Very long content that exceeds normal limits. ".repeat(50);
            var authorId = AuthorId.validateThenCreate("long-author").get();
            var personName = PersonName.validateThenCreate("Long Content Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();

            // When
            Validation<Error, Article> result = mapper.toArticle("long-article", "Long Content Title", longContent,
                author);

            // Then
            assertThat(result.isValid()).isTrue();
            var article = result.get();
            assertThat(article.content().value()).isEqualTo(longContent);
            assertThat(article.content().value().length()).isGreaterThan(2000);
        }
    }

    @Nested
    @DisplayName("Given mapper instance")
    class MapperInstanceTests {

        @Test
        @DisplayName("When accessing INSTANCE, then should return non-null mapper")
        void shouldReturnNonNullMapper_whenAccessingInstance() {
            // When & Then
            assertThat(ArticleMapper.INSTANCE).isNotNull();
            assertThat(ArticleMapper.INSTANCE).isInstanceOf(ArticleMapper.class);
        }

        @Test
        @DisplayName("When accessing INSTANCE multiple times, then should return same instance")
        void shouldReturnSameInstance_whenAccessingMultipleTimes() {
            // When
            var instance1 = ArticleMapper.INSTANCE;
            var instance2 = ArticleMapper.INSTANCE;

            // Then
            assertThat(instance1).isSameAs(instance2);
        }
    }
}
