package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;

/**
 * Unit tests for ApiMapper.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiMapper Tests")
class ApiMapperTest {

    private final ApiMapper mapper = ApiMapper.INSTANCE;

    @Nested
    @DisplayName("Given mapper instance")
    class MapperInstanceTests {

        @Test
        @DisplayName("When accessing INSTANCE, then should return non-null mapper")
        void shouldReturnNonNullMapper_whenAccessingInstance() {
            // When & Then
            assertThat(ApiMapper.INSTANCE).isNotNull();
            assertThat(ApiMapper.INSTANCE).isInstanceOf(ApiMapper.class);
        }

        @Test
        @DisplayName("When accessing INSTANCE multiple times, then should return same instance")
        void shouldReturnSameInstance_whenAccessingMultipleTimes() {
            // When
            var instance1 = ApiMapper.INSTANCE;
            var instance2 = ApiMapper.INSTANCE;

            // Then
            assertThat(instance1).isSameAs(instance2);
        }
    }

    @Nested
    @DisplayName("Given toArticleResponse method")
    class ToArticleResponseTests {

        @Test
        @DisplayName("When mapping ArticleDTO to ApiResponse.Article, then should map all fields correctly")
        void shouldMapAllFields_whenMappingArticleDTO() {
            // Given
            var articleDTO = new ArticleDTO("article-123", "Test Title", "Test Content", "John Doe");

            // When
            ApiResponse.Article result = mapper.toArticleResponse(articleDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(articleDTO.id());
            assertThat(result.title()).isEqualTo(articleDTO.title());
            assertThat(result.content()).isEqualTo(articleDTO.content());
            assertThat(result.author()).isEqualTo(articleDTO.author());
        }

        @Test
        @DisplayName("When mapping ArticleDTO with null values, then should map to null values")
        void shouldMapNullValues_whenMappingArticleDTOWithNulls() {
            // Given
            var articleDTO = new ArticleDTO(null, null, null, null);

            // When
            ApiResponse.Article result = mapper.toArticleResponse(articleDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isNull();
            assertThat(result.title()).isNull();
            assertThat(result.content()).isNull();
            assertThat(result.author()).isNull();
        }

        @Test
        @DisplayName("When mapping ArticleDTO with empty strings, then should map to empty strings")
        void shouldMapEmptyStrings_whenMappingArticleDTOWithEmptyStrings() {
            // Given
            var articleDTO = new ArticleDTO("", "", "", "");

            // When
            ApiResponse.Article result = mapper.toArticleResponse(articleDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEmpty();
            assertThat(result.title()).isEmpty();
            assertThat(result.content()).isEmpty();
            assertThat(result.author()).isEmpty();
        }

        @Test
        @DisplayName("When mapping ArticleDTO with unicode content, then should preserve unicode")
        void shouldPreserveUnicode_whenMappingArticleDTOWithUnicode() {
            // Given
            var articleDTO = new ArticleDTO("unicode-123", "Título con Ñ y Ç",
                "Contenido con caracteres especiales: áéíóú, 中文, 🚀", "José María Azañón");

            // When
            ApiResponse.Article result = mapper.toArticleResponse(articleDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(articleDTO.id());
            assertThat(result.title()).isEqualTo(articleDTO.title());
            assertThat(result.content()).isEqualTo(articleDTO.content());
            assertThat(result.author()).isEqualTo(articleDTO.author());
        }

        @Test
        @DisplayName("When mapping ArticleDTO with long content, then should preserve long content")
        void shouldPreserveLongContent_whenMappingArticleDTOWithLongContent() {
            // Given
            String longContent = "Very long content that exceeds normal limits. ".repeat(100);
            var articleDTO = new ArticleDTO("long-123", "Long Title", longContent, "Long Author");

            // When
            ApiResponse.Article result = mapper.toArticleResponse(articleDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.content()).isEqualTo(longContent);
            assertThat(result.content().length()).isGreaterThan(3000);
        }
    }
}
