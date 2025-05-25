package com.emedina.hexagonal.ref.app.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for ApiResultUtils.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("ApiResultUtils Tests")
class ApiResultUtilsTest {

    @Nested
    @DisplayName("Given createSuccessResponse method")
    class CreateSuccessResponseTests {

        @Test
        @DisplayName("When creating success response with OK status, then should return response with OK status")
        void shouldReturnResponseWithOkStatus_whenCreatingSuccessResponseWithOkStatus() {
            // Given
            var status = HttpStatus.OK;
            var response = new ApiResponse.Article("article-123", "John Doe", "Test Title", "Test Content");

            // When
            ResponseEntity<ApiResponse> result = ApiResultUtils.createSuccessResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isEqualTo(response);
        }

        @Test
        @DisplayName("When creating success response with CREATED status, then should return response with CREATED status")
        void shouldReturnResponseWithCreatedStatus_whenCreatingSuccessResponseWithCreatedStatus() {
            // Given
            var status = HttpStatus.CREATED;
            var response = new ApiResponse.Article("article-123", "John Doe", "Test Title", "Test Content");

            // When
            ResponseEntity<ApiResponse> result = ApiResultUtils.createSuccessResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isEqualTo(response);
        }

        @Test
        @DisplayName("When creating success response with null body, then should return response with null body")
        void shouldReturnResponseWithNullBody_whenCreatingSuccessResponseWithNullBody() {
            // Given
            var status = HttpStatus.OK;
            ApiResponse response = null;

            // When
            ResponseEntity<ApiResponse> result = ApiResultUtils.createSuccessResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("Given createSuccessListResponse method")
    class CreateSuccessListResponseTests {

        @Test
        @DisplayName("When creating success list response with OK status, then should return response with OK status")
        void shouldReturnResponseWithOkStatus_whenCreatingSuccessListResponseWithOkStatus() {
            // Given
            var status = HttpStatus.OK;
            List<ApiResponse> response = List.of(
                new ApiResponse.Article("article-1", "John Doe", "Title 1", "Content 1"),
                new ApiResponse.Article("article-2", "Jane Smith", "Title 2", "Content 2")
            );

            // When
            ResponseEntity<List<ApiResponse>> result = ApiResultUtils.createSuccessListResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isEqualTo(response);
            assertThat(result.getBody()).hasSize(2);
        }

        @Test
        @DisplayName("When creating success list response with empty list, then should return response with empty list")
        void shouldReturnResponseWithEmptyList_whenCreatingSuccessListResponseWithEmptyList() {
            // Given
            var status = HttpStatus.OK;
            var response = List.<ApiResponse>of();

            // When
            ResponseEntity<List<ApiResponse>> result = ApiResultUtils.createSuccessListResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isEmpty();
        }

        @Test
        @DisplayName("When creating success list response with null list, then should return response with null list")
        void shouldReturnResponseWithNullList_whenCreatingSuccessListResponseWithNullList() {
            // Given
            var status = HttpStatus.OK;
            List<ApiResponse> response = null;

            // When
            ResponseEntity<List<ApiResponse>> result = ApiResultUtils.createSuccessListResponse(status, response);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(status);
            assertThat(result.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("Given createFailureResponse method")
    class CreateFailureResponseTests {

        @Test
        @DisplayName("When creating failure response with single problem detail, then should return response with that problem's status")
        void shouldReturnResponseWithProblemStatus_whenCreatingFailureResponseWithSingleProblem() {
            // Given
            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
            problemDetail.setDetail("Test error detail");
            var problemDetails = List.of(problemDetail);
            var uri = URI.create("/api/test");

            // When
            ResponseEntity<List<ProblemDetail>> result = ApiResultUtils.createFailureResponse(problemDetails, uri);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).hasSize(1);
            assertThat(result.getBody().get(0).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(result.getBody().get(0).getDetail()).isEqualTo("Test error detail");
            assertThat(result.getBody().get(0).getType()).isEqualTo(uri);
        }

        @Test
        @DisplayName("When creating failure response with multiple problem details, then should return response with I_AM_A_TEAPOT status")
        void shouldReturnResponseWithTeapotStatus_whenCreatingFailureResponseWithMultipleProblems() {
            // Given
            var problemDetail1 = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
            problemDetail1.setDetail("Error 1");
            var problemDetail2 = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
            problemDetail2.setDetail("Error 2");
            var problemDetails = List.of(problemDetail1, problemDetail2);
            var uri = URI.create("/api/test");

            // When
            ResponseEntity<List<ProblemDetail>> result = ApiResultUtils.createFailureResponse(problemDetails, uri);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
            assertThat(result.getBody()).hasSize(2);
            assertThat(result.getBody().get(0).getType()).isEqualTo(uri);
            assertThat(result.getBody().get(1).getType()).isEqualTo(uri);
        }

        @Test
        @DisplayName("When creating failure response with empty problem details list, then should return response with I_AM_A_TEAPOT status")
        void shouldReturnResponseWithTeapotStatus_whenCreatingFailureResponseWithEmptyProblemsList() {
            // Given
            var problemDetails = List.<ProblemDetail>of();
            var uri = URI.create("/api/test");

            // When
            ResponseEntity<List<ProblemDetail>> result = ApiResultUtils.createFailureResponse(problemDetails, uri);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
            assertThat(result.getBody()).isEmpty();
        }

        @Test
        @DisplayName("When creating failure response with null URI, then should handle null URI gracefully")
        void shouldHandleNullUri_whenCreatingFailureResponseWithNullUri() {
            // Given
            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
            var problemDetails = List.of(problemDetail);
            URI uri = URI.create("about:blank");

            // When
            ResponseEntity<List<ProblemDetail>> result = ApiResultUtils.createFailureResponse(problemDetails, uri);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).hasSize(1);
            assertThat(result.getBody().get(0).getType()).isEqualTo(uri);
        }
    }
}
