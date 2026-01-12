package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ApiResponse hierarchy.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    @Nested
    @DisplayName("Given ApiResponse.Article")
    class ApiResponseArticleTests {

        @Test
        @DisplayName("When creating Article with valid data, then should store all fields correctly")
        void shouldCreateArticle_whenValidDataProvided() {
            // Given
            String id = "article-123";
            String author = "John Doe";
            String title = "Test Article Title";
            String content = "This is the content of the test article";

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.author()).isEqualTo(author);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result).isInstanceOf(ApiResponse.class);
        }

        @Test
        @DisplayName("When creating Article with null values, then should store null values")
        void shouldCreateArticle_whenNullValuesProvided() {
            // Given
            String id = null;
            String author = null;
            String title = null;
            String content = null;

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isNull();
            assertThat(result.author()).isNull();
            assertThat(result.title()).isNull();
            assertThat(result.content()).isNull();
        }

        @Test
        @DisplayName("When creating Article with empty strings, then should store empty strings")
        void shouldCreateArticle_whenEmptyStringsProvided() {
            // Given
            String id = "";
            String author = "";
            String title = "";
            String content = "";

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isEqualTo("");
            assertThat(result.author()).isEqualTo("");
            assertThat(result.title()).isEqualTo("");
            assertThat(result.content()).isEqualTo("");
        }

        @Test
        @DisplayName("When creating Article with unicode content, then should store unicode correctly")
        void shouldCreateArticle_whenUnicodeContentProvided() {
            // Given
            String id = "unicode-article-123";
            String author = "José María Azañón";
            String title = "Título con Ñ y Ç";
            String content = "Contenido con caracteres especiales: áéíóú, 中文, 🚀";

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.author()).isEqualTo(author);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("When creating Article with special characters, then should store special characters correctly")
        void shouldCreateArticle_whenSpecialCharactersProvided() {
            // Given
            String id = "special-article-123";
            String author = "Author with Special Chars @#$";
            String title = "Title with @#$%^&*() chars";
            String content = "Content with special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.author()).isEqualTo(author);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("When creating Article with long content, then should store long content correctly")
        void shouldCreateArticle_whenLongContentProvided() {
            // Given
            String id = "long-article-123";
            String author = "Long Content Author";
            String title = "Article with Very Long Content";
            String content = "Very long content that exceeds normal limits. ".repeat(100);

            // When
            ApiResponse.Article result = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.author()).isEqualTo(author);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.content().length()).isGreaterThan(3000);
        }
    }

    @Nested
    @DisplayName("Given ApiResponse.Article equality and hashCode")
    class ApiResponseArticleEqualityTests {

        @Test
        @DisplayName("When comparing same Articles, then should be equal")
        void shouldBeEqual_whenSameArticlesCompared() {
            // Given
            ApiResponse.Article article1 = new ApiResponse.Article("id-123", "John Doe", "Title", "Content");
            ApiResponse.Article article2 = new ApiResponse.Article("id-123", "John Doe", "Title", "Content");

            // When & Then
            assertThat(article1).isEqualTo(article2);
            assertThat(article1.hashCode()).isEqualTo(article2.hashCode());
        }

        @Test
        @DisplayName("When comparing different Articles, then should not be equal")
        void shouldNotBeEqual_whenDifferentArticlesCompared() {
            // Given
            ApiResponse.Article article1 = new ApiResponse.Article("id-123", "John Doe", "Title", "Content");
            ApiResponse.Article article2 = new ApiResponse.Article("id-789", "John Doe", "Title", "Content");

            // When & Then
            assertThat(article1).isNotEqualTo(article2);
        }

        @Test
        @DisplayName("When comparing Article with null, then should not be equal")
        void shouldNotBeEqual_whenComparedWithNull() {
            // Given
            ApiResponse.Article article = new ApiResponse.Article("id-123", "John Doe", "Title", "Content");

            // When & Then
            assertThat(article).isNotEqualTo(null);
        }

        @Test
        @DisplayName("When comparing Articles with null fields, then should handle null equality correctly")
        void shouldHandleNullEquality_whenNullFieldsCompared() {
            // Given
            ApiResponse.Article article1 = new ApiResponse.Article(null, null, null, null);
            ApiResponse.Article article2 = new ApiResponse.Article(null, null, null, null);
            ApiResponse.Article article3 = new ApiResponse.Article("id", null, null, null);

            // When & Then
            assertThat(article1).isEqualTo(article2);
            assertThat(article1.hashCode()).isEqualTo(article2.hashCode());
            assertThat(article1).isNotEqualTo(article3);
        }

        @Test
        @DisplayName("When comparing Articles with different authors, then should not be equal")
        void shouldNotBeEqual_whenDifferentAuthorsCompared() {
            // Given
            ApiResponse.Article article1 = new ApiResponse.Article("id-123", "John Doe", "Title", "Content");
            ApiResponse.Article article2 = new ApiResponse.Article("id-123", "Jane Smith", "Title", "Content");

            // When & Then
            assertThat(article1).isNotEqualTo(article2);
        }
    }

    @Nested
    @DisplayName("Given ApiResponse.Article toString")
    class ApiResponseArticleToStringTests {

        @Test
        @DisplayName("When calling toString, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            ApiResponse.Article article = new ApiResponse.Article("article-123", "John Doe", "Test Title",
                "Test Content");

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("article-123");
            assertThat(result).contains("John Doe");
            assertThat(result).contains("Test Title");
            assertThat(result).contains("Test Content");
        }

        @Test
        @DisplayName("When calling toString with null values, then should handle nulls gracefully")
        void shouldHandleNulls_whenToStringCalledWithNullValues() {
            // Given
            ApiResponse.Article article = new ApiResponse.Article(null, null, null, null);

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
            ApiResponse.Article article = new ApiResponse.Article("unicode-123", "José María", "Título Español",
                "Contenido con ñ y ç");

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("unicode-123");
            assertThat(result).contains("José María");
            assertThat(result).contains("Título Español");
            assertThat(result).contains("Contenido con ñ y ç");
        }

        @Test
        @DisplayName("When calling toString with special characters, then should display special characters correctly")
        void shouldDisplaySpecialChars_whenToStringCalledWithSpecialCharacters() {
            // Given
            ApiResponse.Article article = new ApiResponse.Article("special-123", "Author@#$%", "Title@#$%",
                "Content@#$%");

            // When
            String result = article.toString();

            // Then
            assertThat(result).contains("Article");
            assertThat(result).contains("special-123");
            assertThat(result).contains("Author@#$%");
            assertThat(result).contains("Title@#$%");
            assertThat(result).contains("Content@#$%");
        }
    }

    @Nested
    @DisplayName("Given ApiResponse.Article field access")
    class ApiResponseArticleFieldAccessTests {

        @Test
        @DisplayName("When accessing fields multiple times, then should return consistent values")
        void shouldReturnConsistentValues_whenFieldsAccessedMultipleTimes() {
            // Given
            ApiResponse.Article article = new ApiResponse.Article("consistent-123", "Consistent Author",
                "Consistent Title", "Consistent Content");

            // When & Then
            assertThat(article.id()).isEqualTo("consistent-123");
            assertThat(article.id()).isEqualTo("consistent-123"); // Second access

            assertThat(article.author()).isEqualTo("Consistent Author");
            assertThat(article.author()).isEqualTo("Consistent Author"); // Second access

            assertThat(article.title()).isEqualTo("Consistent Title");
            assertThat(article.title()).isEqualTo("Consistent Title"); // Second access

            assertThat(article.content()).isEqualTo("Consistent Content");
            assertThat(article.content()).isEqualTo("Consistent Content"); // Second access
        }

        @Test
        @DisplayName("When creating multiple Articles with same data, then should be independent instances")
        void shouldBeIndependentInstances_whenMultipleArticlesCreatedWithSameData() {
            // Given
            String id = "shared-123";
            String author = "Shared Author";
            String title = "Shared Title";
            String content = "Shared Content";

            // When
            ApiResponse.Article article1 = new ApiResponse.Article(id, author, title, content);
            ApiResponse.Article article2 = new ApiResponse.Article(id, author, title, content);

            // Then
            assertThat(article1).isEqualTo(article2); // Equal in value
            assertThat(article1).isNotSameAs(article2); // But different instances
            assertThat(article1.id()).isSameAs(article2.id()); // String interning
            assertThat(article1.author()).isSameAs(article2.author()); // String interning
            assertThat(article1.title()).isSameAs(article2.title()); // String interning
            assertThat(article1.content()).isSameAs(article2.content()); // String interning
        }
    }

}
