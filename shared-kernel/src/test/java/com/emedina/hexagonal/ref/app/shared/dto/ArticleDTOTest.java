package com.emedina.hexagonal.ref.app.shared.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ArticleDTO.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ArticleDTO Tests")
class ArticleDTOTest {

    @Nested
    @DisplayName("Given ArticleDTO creation")
    class ArticleDTOCreationTests {

        @Test
        @DisplayName("When creating ArticleDTO with valid data, then should store all fields correctly")
        void shouldCreateArticleDTO_whenValidDataProvided() {
            // Given
            String id = "article-123";
            String title = "Test Article Title";
            String content = "This is the content of the test article";
            String author = "John Doe";

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.author()).isEqualTo(author);
        }

        @Test
        @DisplayName("When creating ArticleDTO with null values, then should store null values")
        void shouldCreateArticleDTO_whenNullValuesProvided() {
            // Given
            String id = null;
            String title = null;
            String content = null;
            String author = null;

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isNull();
            assertThat(result.title()).isNull();
            assertThat(result.content()).isNull();
            assertThat(result.author()).isNull();
        }

        @Test
        @DisplayName("When creating ArticleDTO with empty strings, then should store empty strings")
        void shouldCreateArticleDTO_whenEmptyStringsProvided() {
            // Given
            String id = "";
            String title = "";
            String content = "";
            String author = "";

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isEqualTo("");
            assertThat(result.title()).isEqualTo("");
            assertThat(result.content()).isEqualTo("");
            assertThat(result.author()).isEqualTo("");
        }

        @Test
        @DisplayName("When creating ArticleDTO with unicode content, then should store unicode correctly")
        void shouldCreateArticleDTO_whenUnicodeContentProvided() {
            // Given
            String id = "unicode-article-123";
            String title = "Título con Ñ y Ç";
            String content = "Contenido con caracteres especiales: áéíóú, 中文, 🚀";
            String author = "José María Azañón";

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.author()).isEqualTo(author);
        }

        @Test
        @DisplayName("When creating ArticleDTO with long content, then should store long content correctly")
        void shouldCreateArticleDTO_whenLongContentProvided() {
            // Given
            String id = "long-article-123";
            String title = "Article with Very Long Content";
            String content = "Very long content that exceeds normal limits. ".repeat(100);
            String author = "Long Content Author";

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.content().length()).isGreaterThan(3000);
            assertThat(result.author()).isEqualTo(author);
        }

        @Test
        @DisplayName("When creating ArticleDTO with special characters, then should store special characters correctly")
        void shouldCreateArticleDTO_whenSpecialCharactersProvided() {
            // Given
            String id = "special-article-123";
            String title = "Title with @#$%^&*() chars";
            String content = "Content with special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";
            String author = "Author with Special Chars @#$";

            // When
            ArticleDTO result = new ArticleDTO(id, title, content, author);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.author()).isEqualTo(author);
        }
    }

    @Nested
    @DisplayName("Given ArticleDTO equality and hashCode")
    class ArticleDTOEqualityTests {

        @Test
        @DisplayName("When comparing same ArticleDTOs, then should be equal")
        void shouldBeEqual_whenSameArticleDTOsCompared() {
            // Given
            ArticleDTO dto1 = new ArticleDTO("id-123", "Title", "Content", "Author");
            ArticleDTO dto2 = new ArticleDTO("id-123", "Title", "Content", "Author");

            // When & Then
            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("When comparing different ArticleDTOs, then should not be equal")
        void shouldNotBeEqual_whenDifferentArticleDTOsCompared() {
            // Given
            ArticleDTO dto1 = new ArticleDTO("id-123", "Title", "Content", "Author");
            ArticleDTO dto2 = new ArticleDTO("id-456", "Title", "Content", "Author");

            // When & Then
            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("When comparing ArticleDTO with null, then should not be equal")
        void shouldNotBeEqual_whenComparedWithNull() {
            // Given
            ArticleDTO dto = new ArticleDTO("id-123", "Title", "Content", "Author");

            // When & Then
            assertThat(dto).isNotEqualTo(null);
        }

        @Test
        @DisplayName("When comparing ArticleDTO with different type, then should not be equal")
        void shouldNotBeEqual_whenComparedWithDifferentType() {
            // Given
            ArticleDTO dto = new ArticleDTO("id-123", "Title", "Content", "Author");
            String differentType = "not an ArticleDTO";

            // When & Then
            assertThat(dto).isNotEqualTo(differentType);
        }

        @Test
        @DisplayName("When comparing ArticleDTOs with null fields, then should handle null equality correctly")
        void shouldHandleNullEquality_whenNullFieldsCompared() {
            // Given
            ArticleDTO dto1 = new ArticleDTO(null, null, null, null);
            ArticleDTO dto2 = new ArticleDTO(null, null, null, null);
            ArticleDTO dto3 = new ArticleDTO("id", null, null, null);

            // When & Then
            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
            assertThat(dto1).isNotEqualTo(dto3);
        }
    }

    @Nested
    @DisplayName("Given ArticleDTO toString")
    class ArticleDTOToStringTests {

        @Test
        @DisplayName("When calling toString, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            ArticleDTO dto = new ArticleDTO("article-123", "Test Title", "Test Content", "Test Author");

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("ArticleDTO");
            assertThat(result).contains("article-123");
            assertThat(result).contains("Test Title");
            assertThat(result).contains("Test Content");
            assertThat(result).contains("Test Author");
        }

        @Test
        @DisplayName("When calling toString with null values, then should handle nulls gracefully")
        void shouldHandleNulls_whenToStringCalledWithNullValues() {
            // Given
            ArticleDTO dto = new ArticleDTO(null, null, null, null);

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("ArticleDTO");
            assertThat(result).contains("null");
        }
    }

    @Nested
    @DisplayName("Given ArticleDTO serialization")
    class ArticleDTOSerializationTests {

        @Test
        @DisplayName("When serializing and deserializing ArticleDTO, then should preserve all data")
        void shouldPreserveData_whenSerializedAndDeserialized() throws Exception {
            // Given
            ArticleDTO original = new ArticleDTO("article-123", "Test Title", "Test Content", "Test Author");

            // When - Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            // When - Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ArticleDTO deserialized = (ArticleDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.id()).isEqualTo(original.id());
            assertThat(deserialized.title()).isEqualTo(original.title());
            assertThat(deserialized.content()).isEqualTo(original.content());
            assertThat(deserialized.author()).isEqualTo(original.author());
        }

        @Test
        @DisplayName("When serializing ArticleDTO with unicode content, then should preserve unicode")
        void shouldPreserveUnicode_whenSerializedWithUnicodeContent() throws Exception {
            // Given
            ArticleDTO original = new ArticleDTO("unicode-123", "Título Español", "Contenido con ñ y ç", "José María");

            // When - Serialize and Deserialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ArticleDTO deserialized = (ArticleDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.title()).isEqualTo("Título Español");
            assertThat(deserialized.content()).isEqualTo("Contenido con ñ y ç");
            assertThat(deserialized.author()).isEqualTo("José María");
        }
    }
}
