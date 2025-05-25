package com.emedina.hexagonal.ref.app.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for AuthorExternalAPIAdapter.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("AuthorExternalAPIAdapter Tests")
class AuthorExternalAPIAdapterTest {

    private RestClient restClient;
    private AuthorExternalAPIAdapter adapter;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class);
        adapter = new AuthorExternalAPIAdapter(restClient);
    }

    @Nested
    @DisplayName("Given lookupAuthor method")
    class LookupAuthorTests {

        @Test
        @DisplayName("When looking up author with valid id, then should return author successfully")
        void shouldReturnAuthor_whenValidIdProvided() {
            // Given
            String authorId = "author-123";

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isEqualTo(authorId);
            assertThat(author.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When looking up author with null id, then should return author with null id")
        void shouldReturnAuthor_whenNullIdProvided() {
            // Given
            String authorId = null;

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isNull();
            assertThat(author.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When looking up author with empty id, then should return author with empty id")
        void shouldReturnAuthor_whenEmptyIdProvided() {
            // Given
            String authorId = "";

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isEqualTo("");
            assertThat(author.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When looking up author with special characters in id, then should return author with special characters")
        void shouldReturnAuthor_whenSpecialCharactersInIdProvided() {
            // Given
            String authorId = "author-@#$%^&*()";

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isEqualTo(authorId);
            assertThat(author.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When looking up author with unicode id, then should return author with unicode id")
        void shouldReturnAuthor_whenUnicodeIdProvided() {
            // Given
            String authorId = "作者-123";

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isEqualTo(authorId);
            assertThat(author.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When looking up author with very long id, then should return author with long id")
        void shouldReturnAuthor_whenVeryLongIdProvided() {
            // Given
            String authorId = "very-long-author-id-that-exceeds-normal-limits-".repeat(10);

            // When
            Either<Error, AuthorDTO> result = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result.isRight()).isTrue();
            AuthorDTO author = result.get();
            assertThat(author.id()).isEqualTo(authorId);
            assertThat(author.name()).isEqualTo("William Shakespeare");
            assertThat(author.id().length()).isGreaterThan(400);
        }

        @Test
        @DisplayName("When looking up multiple authors with different ids, then should return different authors with same name")
        void shouldReturnDifferentAuthors_whenDifferentIdsProvided() {
            // Given
            String authorId1 = "shakespeare-123";
            String authorId2 = "dickens-456";

            // When
            Either<Error, AuthorDTO> result1 = adapter.lookupAuthor(authorId1);
            Either<Error, AuthorDTO> result2 = adapter.lookupAuthor(authorId2);

            // Then
            assertThat(result1.isRight()).isTrue();
            assertThat(result2.isRight()).isTrue();

            AuthorDTO author1 = result1.get();
            AuthorDTO author2 = result2.get();

            assertThat(author1.id()).isEqualTo(authorId1);
            assertThat(author2.id()).isEqualTo(authorId2);
            assertThat(author1.name()).isEqualTo("William Shakespeare");
            assertThat(author2.name()).isEqualTo("William Shakespeare");
            assertThat(author1).isNotEqualTo(author2); // Different because of different IDs
        }
    }

    @Nested
    @DisplayName("Given adapter construction")
    class AdapterConstructionTests {

        @Test
        @DisplayName("When creating adapter with RestClient, then should create successfully")
        void shouldCreateAdapter_whenRestClientProvided() {
            // Given
            RestClient mockRestClient = mock(RestClient.class);

            // When
            AuthorExternalAPIAdapter newAdapter = new AuthorExternalAPIAdapter(mockRestClient);

            // Then
            assertThat(newAdapter).isNotNull();
        }

        @Test
        @DisplayName("When creating adapter with null RestClient, then should create successfully")
        void shouldCreateAdapter_whenNullRestClientProvided() {
            // Given
            RestClient nullRestClient = null;

            // When
            AuthorExternalAPIAdapter newAdapter = new AuthorExternalAPIAdapter(nullRestClient);

            // Then
            assertThat(newAdapter).isNotNull();
            // The adapter should still work since it doesn't actually use the RestClient in the current implementation
            Either<Error, AuthorDTO> result = newAdapter.lookupAuthor("test-id");
            assertThat(result.isRight()).isTrue();
        }
    }

    @Nested
    @DisplayName("Given adapter behavior consistency")
    class AdapterBehaviorConsistencyTests {

        @Test
        @DisplayName("When calling lookupAuthor multiple times with same id, then should return consistent results")
        void shouldReturnConsistentResults_whenCalledMultipleTimesWithSameId() {
            // Given
            String authorId = "consistent-test-123";

            // When
            Either<Error, AuthorDTO> result1 = adapter.lookupAuthor(authorId);
            Either<Error, AuthorDTO> result2 = adapter.lookupAuthor(authorId);
            Either<Error, AuthorDTO> result3 = adapter.lookupAuthor(authorId);

            // Then
            assertThat(result1.isRight()).isTrue();
            assertThat(result2.isRight()).isTrue();
            assertThat(result3.isRight()).isTrue();

            AuthorDTO author1 = result1.get();
            AuthorDTO author2 = result2.get();
            AuthorDTO author3 = result3.get();

            assertThat(author1).isEqualTo(author2);
            assertThat(author2).isEqualTo(author3);
            assertThat(author1.id()).isEqualTo(authorId);
            assertThat(author1.name()).isEqualTo("William Shakespeare");
        }

        @Test
        @DisplayName("When calling lookupAuthor with different case ids, then should preserve case")
        void shouldPreserveCase_whenDifferentCaseIdsProvided() {
            // Given
            String lowerCaseId = "author-lowercase";
            String upperCaseId = "AUTHOR-UPPERCASE";
            String mixedCaseId = "Author-MixedCase";

            // When
            Either<Error, AuthorDTO> result1 = adapter.lookupAuthor(lowerCaseId);
            Either<Error, AuthorDTO> result2 = adapter.lookupAuthor(upperCaseId);
            Either<Error, AuthorDTO> result3 = adapter.lookupAuthor(mixedCaseId);

            // Then
            assertThat(result1.isRight()).isTrue();
            assertThat(result2.isRight()).isTrue();
            assertThat(result3.isRight()).isTrue();

            assertThat(result1.get().id()).isEqualTo(lowerCaseId);
            assertThat(result2.get().id()).isEqualTo(upperCaseId);
            assertThat(result3.get().id()).isEqualTo(mixedCaseId);

            // All should have the same name
            assertThat(result1.get().name()).isEqualTo("William Shakespeare");
            assertThat(result2.get().name()).isEqualTo("William Shakespeare");
            assertThat(result3.get().name()).isEqualTo("William Shakespeare");
        }
    }
}
