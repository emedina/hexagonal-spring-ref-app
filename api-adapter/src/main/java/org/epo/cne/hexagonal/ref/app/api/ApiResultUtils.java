package org.epo.cne.hexagonal.ref.app.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;

/**
 * Utility class to create the {@link ResponseEntity} based on an {@link ApiResponse} for successful responses
 * or a {@link ProblemDetail} for failure responses.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ApiResultUtils {

    static ResponseEntity<ApiResponse> createSuccessResponse(final HttpStatusCode status,
                                                             final ApiResponse response) {
        return ResponseEntity.status(status).body(response);
    }

    static ResponseEntity<List<ApiResponse>> createSuccessListResponse(final HttpStatusCode status,
                                                                       final List<ApiResponse> response) {
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Creates a failure response with a list of {@link ProblemDetail}, setting the originating {@link URI}.
     *
     * @param problemDetails the list of problems
     * @param uri            the originating URI
     * @return the failure response
     */
    static ResponseEntity<List<ProblemDetail>> createFailureResponse(final List<ProblemDetail> problemDetails, final URI uri) {
        return ResponseEntity
                // If there is only one problem, and it has a status code, return that status code.
                .status(problemDetails.size() == 1 ? HttpStatusCode.valueOf(problemDetails.get(0).getStatus()) : HttpStatus.I_AM_A_TEAPOT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(problemDetails.stream().map(detail -> {
                    detail.setType(uri);
                    return detail;
                }).toList());
    }

}
