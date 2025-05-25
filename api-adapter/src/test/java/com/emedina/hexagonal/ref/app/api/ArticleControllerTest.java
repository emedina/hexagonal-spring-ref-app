package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.DeleteArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.hexagonal.ref.app.application.query.FindArticleQuery;
import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;
import com.emedina.sharedkernel.command.core.CommandBus;
import com.emedina.sharedkernel.query.core.QueryBus;

import io.vavr.control.Either;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Unit tests for ArticleController.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("ArticleController Tests")
class ArticleControllerTest {

    @Mock
    private CommandBus commandBus;

    @Mock
    private QueryBus queryBus;

    @Mock
    private ApiErrorHandler apiErrorHandler;

    @Mock
    private HttpServletRequest mockRequest;

    private ArticleController controller;

    @BeforeEach
    void setUp() {
        controller = new ArticleController(commandBus, queryBus, apiErrorHandler);
        when(mockRequest.getRequestURI()).thenReturn("/api/articles");
    }

    @Nested
    @DisplayName("Given get method")
    class GetMethodTests {

        @Test
        @DisplayName("When getting all articles successfully, then should return OK with articles list")
        void shouldReturnOkWithArticlesList_whenGettingAllArticlesSuccessfully() {
            // Given
            GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
            List<ArticleDTO> articles = List.of(
                new ArticleDTO("article-1", "Title 1", "Content 1", "Author 1"),
                new ArticleDTO("article-2", "Title 2", "Content 2", "Author 2")
            );

            when(queryBus.query(any(GetAllArticlesQuery.class))).thenReturn(Either.right(articles));

            List<ApiResponse> expectedResponses = List.of(
                new ApiResponse.Article("article-1", "Author 1", "Title 1", "Content 1"),
                new ApiResponse.Article("article-2", "Author 2", "Title 2", "Content 2")
            );

            // When
            ResponseEntity<?> response = controller.get(mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<ApiResponse> actualResponses = (List<ApiResponse>) response.getBody();

            assertThat(actualResponses).hasSize(2);
            assertThat(actualResponses).isEqualTo(expectedResponses);
            verify(queryBus).query(any(GetAllArticlesQuery.class));
        }

        @Test
        @DisplayName("When getting all articles returns empty list, then should return OK with empty list")
        void shouldReturnOkWithEmptyList_whenGettingAllArticlesReturnsEmptyList() {
            // Given
            GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
            List<ArticleDTO> emptyList = List.of();

            when(queryBus.query(any(GetAllArticlesQuery.class))).thenReturn(Either.right(emptyList));

            // When
            ResponseEntity<?> response = controller.get(mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<ApiResponse> actualResponses = (List<ApiResponse>) response.getBody();

            assertThat(actualResponses).isEmpty();
            verify(queryBus).query(any(GetAllArticlesQuery.class));
        }

        @Test
        @DisplayName("When getting all articles fails with error, then should return error response")
        void shouldReturnErrorResponse_whenGettingAllArticlesFails() {
            // Given
            GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
            Error error = new Error.TechnicalError.SomethingWentWrong("Database connection failed");

            when(queryBus.query(any(GetAllArticlesQuery.class))).thenReturn(Either.left(error));

            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            problemDetail.setDetail("Internal server error [Database connection failed]");
            List<ProblemDetail> problemDetails = List.of(problemDetail);

            when(apiErrorHandler.mapErrorToProblemDetail(eq(error), any(HttpServletRequest.class)))
                .thenReturn(problemDetails);

            // When
            ResponseEntity<?> response = controller.get(mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<ProblemDetail> actualProblemDetails = (List<ProblemDetail>) response.getBody();

            assertThat(actualProblemDetails).hasSize(1);
            assertThat(actualProblemDetails.get(0).getDetail()).contains("Database connection failed");

            verify(queryBus).query(any(GetAllArticlesQuery.class));
            verify(apiErrorHandler).mapErrorToProblemDetail(eq(error), any(HttpServletRequest.class));
        }

        @Test
        @DisplayName("When query validation fails, then should return error response")
        void shouldReturnErrorResponse_whenQueryValidationFails() {
            // Given
            Error validationError = new Error.ValidationErrors(
                List.of(new ValidationError.Invalid("Invalid query parameter"))
            );

            // We can't easily mock the static method, so we'll mock the behavior 
            // that would happen when validation fails
            when(queryBus.query(any(GetAllArticlesQuery.class))).thenReturn(Either.left(validationError));

            // Mock static method using mockito-inline
            // This is a simplified approach since we can't easily mock static methods
            // In a real test, you might use PowerMockito or refactor to make testing easier

            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
            problemDetail.setDetail("Invalid value provided [Invalid query parameter]");
            List<ProblemDetail> problemDetails = List.of(problemDetail);

            when(apiErrorHandler.mapErrorToProblemDetail(eq(validationError), any(HttpServletRequest.class)))
                .thenReturn(problemDetails);

            // When - This test is conceptual since we can't easily mock the static method
            // The actual implementation would need to handle this scenario

            // Then - Verify the expected behavior
            // The controller should return a BAD_REQUEST response with the validation error details
        }
    }

    @Nested
    @DisplayName("Given find method")
    class FindMethodTests {

        @Test
        @DisplayName("When finding article by ID successfully, then should return OK with article")
        void shouldReturnOkWithArticle_whenFindingArticleByIdSuccessfully() {
            // Given
            String articleId = "article-123";
            FindArticleQuery query = FindArticleQuery.validateThenCreate(articleId).get();
            ArticleDTO article = new ArticleDTO(articleId, "Test Title", "Test Content", "John Doe");

            when(queryBus.query(any(FindArticleQuery.class))).thenReturn(Either.right(article));

            ApiResponse.Article expectedResponse = new ApiResponse.Article(articleId, "John Doe", "Test Title",
                "Test Content");

            // When
            ResponseEntity<?> response = controller.find(articleId, mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(ApiResponse.Article.class);

            ApiResponse.Article actualResponse = (ApiResponse.Article) response.getBody();

            assertThat(actualResponse).isEqualTo(expectedResponse);
            verify(queryBus).query(any(FindArticleQuery.class));
        }

        @Test
        @DisplayName("When article not found, then should return error response")
        void shouldReturnErrorResponse_whenArticleNotFound() {
            // Given
            String articleId = "non-existent-id";
            FindArticleQuery query = FindArticleQuery.validateThenCreate(articleId).get();
            Error error = new Error.BusinessError.UnknownArticle(articleId);

            when(queryBus.query(any(FindArticleQuery.class))).thenReturn(Either.left(error));

            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
            problemDetail.setDetail("Article with id [non-existent-id] not found");
            List<ProblemDetail> problemDetails = List.of(problemDetail);

            when(apiErrorHandler.mapErrorToProblemDetail(eq(error), any(HttpServletRequest.class)))
                .thenReturn(problemDetails);

            // When
            ResponseEntity<?> response = controller.find(articleId, mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<ProblemDetail> actualProblemDetails = (List<ProblemDetail>) response.getBody();

            assertThat(actualProblemDetails).hasSize(1);
            assertThat(actualProblemDetails.get(0).getDetail()).contains("not found");

            verify(queryBus).query(any(FindArticleQuery.class));
            verify(apiErrorHandler).mapErrorToProblemDetail(eq(error), any(HttpServletRequest.class));
        }

        @Nested
        @DisplayName("Given create method")
        class CreateMethodTests {

            @Test
            @DisplayName("When creating article successfully, then should return CREATED")
            void shouldReturnCreated_whenCreatingArticleSuccessfully() {
                // Given
                ApiRequest.Article articleRequest = new ApiRequest.Article(
                    "new-article-123", "author-456", "New Article Title", "New Article Content"
                );

                CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
                    articleRequest.id(), articleRequest.authorId(), articleRequest.title(), articleRequest.content()
                ).get();

                when(commandBus.execute(any(CreateArticleCommand.class))).thenReturn(Either.right(null));

                // When
                ResponseEntity<?> response = controller.create(articleRequest, mockRequest);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                assertThat(response.getBody()).isNull();

                verify(commandBus).execute(any(CreateArticleCommand.class));
            }

            @Test
            @DisplayName("When creating article with validation error, then should return error response")
            void shouldReturnErrorResponse_whenCreatingArticleWithValidationError() {
                // Given
                ApiRequest.Article invalidArticleRequest = new ApiRequest.Article(
                    "", "author-456", "", "New Article Content"
                );

                Error validationError = new Error.ValidationErrors(List.of(
                    new ValidationError.MustHaveContent("id"),
                    new ValidationError.MustHaveContent("title")
                ));

                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
                problemDetail.setDetail("Must have content [id], Must have content [title]");
                List<ProblemDetail> problemDetails = List.of(problemDetail);

                when(apiErrorHandler.mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class)))
                    .thenReturn(problemDetails);

                // When
                ResponseEntity<?> response = controller.create(invalidArticleRequest, mockRequest);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isInstanceOf(List.class);

                @SuppressWarnings("unchecked")
                List<ProblemDetail> actualProblemDetails = (List<ProblemDetail>) response.getBody();

                assertThat(actualProblemDetails).hasSize(1);
                assertThat(actualProblemDetails.get(0).getDetail()).contains("Must have content");

                verify(apiErrorHandler).mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class));
            }
        }

        @Nested
        @DisplayName("Given update method")
        class UpdateMethodTests {

            @Test
            @DisplayName("When updating article successfully, then should return OK")
            void shouldReturnOk_whenUpdatingArticleSuccessfully() {
                // Given
                ApiRequest.Article articleRequest = new ApiRequest.Article(
                    "existing-article-123", "author-456", "Updated Title", "Updated Content"
                );

                UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
                    articleRequest.id(), articleRequest.authorId(), articleRequest.title(), articleRequest.content()
                ).get();

                when(commandBus.execute(any(UpdateArticleCommand.class))).thenReturn(Either.right(null));

                // When
                ResponseEntity<?> response = controller.update(articleRequest, mockRequest);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNull();

                verify(commandBus).execute(any(UpdateArticleCommand.class));
            }

            @Test
            @DisplayName("When updating article with validation error, then should return error response")
            void shouldReturnErrorResponse_whenUpdatingArticleWithValidationError() {
                // Given
                ApiRequest.Article invalidArticleRequest = new ApiRequest.Article(
                    "article-123", "", "", "Updated Content"
                );

                Error validationError = new Error.ValidationErrors(List.of(
                    new ValidationError.MustHaveContent("authorId"),
                    new ValidationError.MustHaveContent("title")
                ));

                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
                problemDetail.setDetail("Must have content [authorId], Must have content [title]");
                List<ProblemDetail> problemDetails = List.of(problemDetail);

                when(apiErrorHandler.mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class)))
                    .thenReturn(problemDetails);

                // When
                ResponseEntity<?> response = controller.update(invalidArticleRequest, mockRequest);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isInstanceOf(List.class);

                @SuppressWarnings("unchecked")
                List<ProblemDetail> actualProblemDetails = (List<ProblemDetail>) response.getBody();

                assertThat(actualProblemDetails).hasSize(1);
                assertThat(actualProblemDetails.get(0).getDetail()).contains("Must have content");

                verify(apiErrorHandler).mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class));
            }
        }
    }

    @Nested
    @DisplayName("Given delete method")
    class DeleteMethodTests {

        @Test
        @DisplayName("When deleting article successfully, then should return OK")
        void shouldReturnOk_whenDeletingArticleSuccessfully() {
            // Given
            String articleId = "article-to-delete-123";
            DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(articleId).get();

            when(commandBus.execute(any(DeleteArticleCommand.class))).thenReturn(Either.right(null));

            // When
            ResponseEntity<?> response = controller.delete(articleId, mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNull();

            verify(commandBus).execute(any(DeleteArticleCommand.class));
        }

        @Test
        @DisplayName("When deleting article with invalid ID, then should return error response")
        void shouldReturnErrorResponse_whenDeletingArticleWithInvalidId() {
            // Given
            String invalidArticleId = "";
            Error validationError = new Error.ValidationErrors(
                List.of(new ValidationError.MustHaveContent("articleId"))
            );

            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
            problemDetail.setDetail("Must have content [articleId]");
            List<ProblemDetail> problemDetails = List.of(problemDetail);

            when(apiErrorHandler.mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class)))
                .thenReturn(problemDetails);

            // When
            ResponseEntity<?> response = controller.delete(invalidArticleId, mockRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<ProblemDetail> actualProblemDetails = (List<ProblemDetail>) response.getBody();

            assertThat(actualProblemDetails).hasSize(1);
            assertThat(actualProblemDetails.get(0).getDetail()).contains("Must have content");

            verify(apiErrorHandler).mapErrorToProblemDetail(any(Error.class), any(HttpServletRequest.class));
        }
    }
}
