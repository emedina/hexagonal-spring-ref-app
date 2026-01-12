package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ApiRequest hierarchy.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiRequest Tests")
class ApiRequestTest {

    @Nested
    @DisplayName("Given ApiRequest.Article")
    class ApiRequestArticleTests {

        @Test
        @DisplayName("When creating Article with valid data, then should store all fields correctly")
        void shouldCreateArticle_whenValidDataProvided() {
            // Given
            String id = "article-123";
            String authorId = "author-456";
            String title = "Test Article Title";
            String content = "This is the content of the test article";

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.authorId()).isEqualTo(authorId);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result).isInstanceOf(ApiRequest.class);
        }

        @Test
        @DisplayName("When creating Article with null values, then should store null values")
        void shouldCreateArticle_whenNullValuesProvided() {
            // Given
            String id = null;
            String authorId = null;
            String title = null;
            String content = null;

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isNull();
            assertThat(result.authorId()).isNull();
            assertThat(result.title()).isNull();
            assertThat(result.content()).isNull();
        }

        @Test
        @DisplayName("When creating Article with empty strings, then should store empty strings")
        void shouldCreateArticle_whenEmptyStringsProvided() {
            // Given
            String id = "";
            String authorId = "";
            String title = "";
            String content = "";

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isEqualTo("");
            assertThat(result.authorId()).isEqualTo("");
            assertThat(result.title()).isEqualTo("");
            assertThat(result.content()).isEqualTo("");
        }

        @Test
        @DisplayName("When creating Article with unicode content, then should store unicode correctly")
        void shouldCreateArticle_whenUnicodeContentProvided() {
            // Given
            String id = "unicode-article-123";
            String authorId = "unicode-author-456";
            String title = "Título con Ñ y Ç";
            String content = "Contenido con caracteres especiales: áéíóú, 中文, 🚀";

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.authorId()).isEqualTo(authorId);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("When creating Article with special characters, then should store special characters correctly")
        void shouldCreateArticle_whenSpecialCharactersProvided() {
            // Given
            String id = "special-article-123";
            String authorId = "special-author-456";
            String title = "Title with @#$%^&*() chars";
            String content = "Content with special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.authorId()).isEqualTo(authorId);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("When creating Article with long content, then should store long content correctly")
        void shouldCreateArticle_whenLongContentProvided() {
            // Given
            String id = "long-article-123";
            String authorId = "long-author-456";
            String title = "Article with Very Long Content";
            String content = "Very long content that exceeds normal limits. ".repeat(100);

            // When
            ApiRequest.Article result = new ApiRequest.Article(id, authorId, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.authorId()).isEqualTo(authorId);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.content().length()).isGreaterThan(3000);
        }
    }

    @Nested
    @DisplayName("Given ApiRequest.Article equality and hashCode")
    class ApiRequestArticleEqualityTests {

        @Test
        @DisplayName("When comparing same Articles, then should be equal")
        void shouldBeEqual_whenSameArticlesCompared() {
            // Given
            ApiRequest.Article article1 = new ApiRequest.Article("id-123", "author-456", "Title", "Content");
            ApiRequest.Article article2 = new ApiRequest.Article("id-123", "author-456", "Title", "Content");

            // When & Then
            assertThat(article1).isEqualTo(article2);
            assertThat(article1.hashCode()).isEqualTo(article2.hashCode());
        }

        @Test
        @DisplayName("When comparing different Articles, then should not be equal")
        void shouldNotBeEqual_whenDifferentArticlesCompared() {
            // Given
            ApiRequest.Article article1 = new ApiRequest.Article("id-123", "author-456", "Title", "Content");
            ApiRequest.Article article2 = new ApiRequest.Article("id-789", "author-456", "Title", "Content");

            // When & Then
            assertThat(article1).isNotEqualTo(article2);
        }

        @Test
        @DisplayName("When comparing Article with null, then should not be equal")
        void shouldNotBeEqual_whenComparedWithNull() {
            // Given
            ApiRequest.Article article = new ApiRequest.Article("id-123", "author-456", "Title", "Content");

            // When & Then
            assertThat(article).isNotEqualTo(null);
        }

        @Test
        @DisplayName("When comparing Articles with null fields, then should handle null equality correctly")
        void shouldHandleNullEquality_whenNullFieldsCompared() {
            // Given
            ApiRequest.Article article1 = new ApiRequest.Article(null, null, null, null);
            ApiRequest.Article article2 = new ApiRequest.Article(null, null, null, null);
            ApiRequest.Article article3 = new ApiRequest.Article("id", null, null, null);

            // When & Then
            assertThat(article1).isEqualTo(article2);
            assertThat(article1.hashCode()).isEqualTo(article2.hashCode());
            assertThat(article1).isNotEqualTo(article3);
        }
    }

    @Nested
    @DisplayName("Given ApiRequest.Article toString")
    class ApiRequestArticleToStringTests {

        @Test
        @DisplayName("When calling toString, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            ApiRequest.Article article = new ApiRequest.Article("article-123", "author-456", "Test Title",
                "Test Content");

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("article-123");
            assertThat(result).contains("author-456");
            assertThat(result).contains("Test Title");
            assertThat(result).contains("Test Content");
        }

        @Test
        @DisplayName("When calling toString with null values, then should handle nulls gracefully")
        void shouldHandleNulls_whenToStringCalledWithNullValues() {
            // Given
            ApiRequest.Article article = new ApiRequest.Article(null, null, null, null);

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("null");
        }

        @Test
        @DisplayName("When calling toString with unicode content, then should display unicode correctly")
        void shouldDisplayUnicode_whenToStringCalledWithUnicodeContent() {
            // Given
            ApiRequest.Article article = new ApiRequest.Article("unicode-123", "unicode-456", "Título Español",
                "Contenido con ñ y ç");

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("unicode-123");
            assertThat(result).contains("unicode-456");
            assertThat(result).contains("Título Español");
            assertThat(result).contains("Contenido con ñ y ç");
        }
    }

}
