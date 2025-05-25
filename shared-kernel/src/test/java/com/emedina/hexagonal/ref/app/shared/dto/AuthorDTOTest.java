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
 * Unit tests for AuthorDTO.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("AuthorDTO Tests")
class AuthorDTOTest {

    @Nested
    @DisplayName("Given AuthorDTO creation")
    class AuthorDTOCreationTests {

        @Test
        @DisplayName("When creating AuthorDTO with valid data, then should store all fields correctly")
        void shouldCreateAuthorDTO_whenValidDataProvided() {
            // Given
            String id = "author-123";
            String name = "John Doe";

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("When creating AuthorDTO with null values, then should store null values")
        void shouldCreateAuthorDTO_whenNullValuesProvided() {
            // Given
            String id = null;
            String name = null;

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isNull();
            assertThat(result.name()).isNull();
        }

        @Test
        @DisplayName("When creating AuthorDTO with empty strings, then should store empty strings")
        void shouldCreateAuthorDTO_whenEmptyStringsProvided() {
            // Given
            String id = "";
            String name = "";

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo("");
            assertThat(result.name()).isEqualTo("");
        }

        @Test
        @DisplayName("When creating AuthorDTO with unicode name, then should store unicode correctly")
        void shouldCreateAuthorDTO_whenUnicodeNameProvided() {
            // Given
            String id = "unicode-author-123";
            String name = "José María Azañón";

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("When creating AuthorDTO with special characters, then should store special characters correctly")
        void shouldCreateAuthorDTO_whenSpecialCharactersProvided() {
            // Given
            String id = "special-author-123";
            String name = "Author with Special Chars @#$%^&*()";

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("When creating AuthorDTO with long name, then should store long name correctly")
        void shouldCreateAuthorDTO_whenLongNameProvided() {
            // Given
            String id = "long-name-author-123";
            String name = "Very Long Author Name That Exceeds Normal Limits ".repeat(10);

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.name()).isEqualTo(name);
            assertThat(result.name().length()).isGreaterThan(400);
        }

        @Test
        @DisplayName("When creating AuthorDTO with mixed case and numbers, then should store correctly")
        void shouldCreateAuthorDTO_whenMixedCaseAndNumbersProvided() {
            // Given
            String id = "Author123-ID";
            String name = "John Doe Jr. III";

            // When
            AuthorDTO result = new AuthorDTO(id, name);

            // Then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.name()).isEqualTo(name);
        }
    }

    @Nested
    @DisplayName("Given AuthorDTO equality and hashCode")
    class AuthorDTOEqualityTests {

        @Test
        @DisplayName("When comparing same AuthorDTOs, then should be equal")
        void shouldBeEqual_whenSameAuthorDTOsCompared() {
            // Given
            AuthorDTO dto1 = new AuthorDTO("author-123", "John Doe");
            AuthorDTO dto2 = new AuthorDTO("author-123", "John Doe");

            // When & Then
            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("When comparing different AuthorDTOs by id, then should not be equal")
        void shouldNotBeEqual_whenDifferentIdCompared() {
            // Given
            AuthorDTO dto1 = new AuthorDTO("author-123", "John Doe");
            AuthorDTO dto2 = new AuthorDTO("author-456", "John Doe");

            // When & Then
            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("When comparing different AuthorDTOs by name, then should not be equal")
        void shouldNotBeEqual_whenDifferentNameCompared() {
            // Given
            AuthorDTO dto1 = new AuthorDTO("author-123", "John Doe");
            AuthorDTO dto2 = new AuthorDTO("author-123", "Jane Smith");

            // When & Then
            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("When comparing AuthorDTO with null, then should not be equal")
        void shouldNotBeEqual_whenComparedWithNull() {
            // Given
            AuthorDTO dto = new AuthorDTO("author-123", "John Doe");

            // When & Then
            assertThat(dto).isNotEqualTo(null);
        }

        @Test
        @DisplayName("When comparing AuthorDTO with different type, then should not be equal")
        void shouldNotBeEqual_whenComparedWithDifferentType() {
            // Given
            AuthorDTO dto = new AuthorDTO("author-123", "John Doe");
            String differentType = "not an AuthorDTO";

            // When & Then
            assertThat(dto).isNotEqualTo(differentType);
        }

        @Test
        @DisplayName("When comparing AuthorDTOs with null fields, then should handle null equality correctly")
        void shouldHandleNullEquality_whenNullFieldsCompared() {
            // Given
            AuthorDTO dto1 = new AuthorDTO(null, null);
            AuthorDTO dto2 = new AuthorDTO(null, null);
            AuthorDTO dto3 = new AuthorDTO("id", null);
            AuthorDTO dto4 = new AuthorDTO(null, "name");

            // When & Then
            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
            assertThat(dto1).isNotEqualTo(dto3);
            assertThat(dto1).isNotEqualTo(dto4);
            assertThat(dto3).isNotEqualTo(dto4);
        }

        @Test
        @DisplayName("When comparing AuthorDTOs with unicode content, then should handle unicode equality correctly")
        void shouldHandleUnicodeEquality_whenUnicodeContentCompared() {
            // Given
            AuthorDTO dto1 = new AuthorDTO("unicode-123", "José María Ñoño");
            AuthorDTO dto2 = new AuthorDTO("unicode-123", "José María Ñoño");
            AuthorDTO dto3 = new AuthorDTO("unicode-123", "Jose Maria Nono");

            // When & Then
            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
            assertThat(dto1).isNotEqualTo(dto3);
        }
    }

    @Nested
    @DisplayName("Given AuthorDTO toString")
    class AuthorDTOToStringTests {

        @Test
        @DisplayName("When calling toString, then should return meaningful representation")
        void shouldReturnMeaningfulString_whenToStringCalled() {
            // Given
            AuthorDTO dto = new AuthorDTO("author-123", "John Doe");

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("AuthorDTO");
            assertThat(result).contains("author-123");
            assertThat(result).contains("John Doe");
        }

        @Test
        @DisplayName("When calling toString with null values, then should handle nulls gracefully")
        void shouldHandleNulls_whenToStringCalledWithNullValues() {
            // Given
            AuthorDTO dto = new AuthorDTO(null, null);

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("AuthorDTO");
            assertThat(result).contains("null");
        }

        @Test
        @DisplayName("When calling toString with unicode content, then should display unicode correctly")
        void shouldDisplayUnicode_whenToStringCalledWithUnicodeContent() {
            // Given
            AuthorDTO dto = new AuthorDTO("unicode-author", "José María Ñoño");

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("AuthorDTO");
            assertThat(result).contains("unicode-author");
            assertThat(result).contains("José María Ñoño");
        }

        @Test
        @DisplayName("When calling toString with special characters, then should display special characters correctly")
        void shouldDisplaySpecialChars_whenToStringCalledWithSpecialCharacters() {
            // Given
            AuthorDTO dto = new AuthorDTO("special-123", "Author@#$%");

            // When
            String result = dto.toString();

            // Then
            assertThat(result).contains("AuthorDTO");
            assertThat(result).contains("special-123");
            assertThat(result).contains("Author@#$%");
        }
    }

    @Nested
    @DisplayName("Given AuthorDTO serialization")
    class AuthorDTOSerializationTests {

        @Test
        @DisplayName("When serializing and deserializing AuthorDTO, then should preserve all data")
        void shouldPreserveData_whenSerializedAndDeserialized() throws Exception {
            // Given
            AuthorDTO original = new AuthorDTO("author-123", "John Doe");

            // When - Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            // When - Deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            AuthorDTO deserialized = (AuthorDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.id()).isEqualTo(original.id());
            assertThat(deserialized.name()).isEqualTo(original.name());
        }

        @Test
        @DisplayName("When serializing AuthorDTO with unicode content, then should preserve unicode")
        void shouldPreserveUnicode_whenSerializedWithUnicodeContent() throws Exception {
            // Given
            AuthorDTO original = new AuthorDTO("unicode-123", "José María Ñoño");

            // When - Serialize and Deserialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            AuthorDTO deserialized = (AuthorDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.name()).isEqualTo("José María Ñoño");
        }

        @Test
        @DisplayName("When serializing AuthorDTO with null values, then should preserve nulls")
        void shouldPreserveNulls_whenSerializedWithNullValues() throws Exception {
            // Given
            AuthorDTO original = new AuthorDTO(null, null);

            // When - Serialize and Deserialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            AuthorDTO deserialized = (AuthorDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.id()).isNull();
            assertThat(deserialized.name()).isNull();
        }

        @Test
        @DisplayName("When serializing AuthorDTO with special characters, then should preserve special characters")
        void shouldPreserveSpecialChars_whenSerializedWithSpecialCharacters() throws Exception {
            // Given
            AuthorDTO original = new AuthorDTO("special-123", "Author@#$%^&*()");

            // When - Serialize and Deserialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            AuthorDTO deserialized = (AuthorDTO) ois.readObject();
            ois.close();

            // Then
            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.name()).isEqualTo("Author@#$%^&*()");
        }
    }
}
