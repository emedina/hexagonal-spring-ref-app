package org.epo.cne.hexagonal.ref.app.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API interface declaring all available features as REST endpoints, together with metadata annotations
 * for OpenAPI spec documentation.
 * <p>
 * Notice the usage of the sealed interface feature in order to enhance the control over class inheritance
 * and improve the design of class hierarchies.
 * </p>
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@RequestMapping(value = "/api/articles", produces = APPLICATION_JSON_VALUE)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "401", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "403", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "404", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "412", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "418", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "500", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "503", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
})
sealed interface ArticleApi permits ArticleController {

    @GetMapping(path = "/{articleId}")
    @Operation(
            summary = "Retrieve an article by its identifier",
            description = "Retrieves an article by its identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "An article with the provided identifier was found"),
            @ApiResponse(responseCode = "400", description = "The provided identifier is invalid"),
            @ApiResponse(responseCode = "404", description = "The identifier is unknown to the system"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    ResponseEntity<?> get(final String articleId, final HttpServletRequest request);

    @PostMapping
    @Operation(
            summary = "Creates an article",
            description = "Creates an article"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The article was created successfully"),
            @ApiResponse(responseCode = "400", description = "The provided data is invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    ResponseEntity<?> create(final ArticleRequest articleRequest, final HttpServletRequest request);

    @PutMapping(path = "/{articleId}")
    @Operation(
            summary = "Updates an article",
            description = "Updates an article"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The article was updated successfully"),
            @ApiResponse(responseCode = "400", description = "The provided data is invalid"),
            @ApiResponse(responseCode = "404", description = "The identifier is unknown to the system"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    ResponseEntity<?> update(final ArticleRequest articleRequest, final HttpServletRequest request);

    @DeleteMapping(path = "/{articleId}")
    @Operation(
            summary = "Deletes an article",
            description = "Deletes an article"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The article was deleted successfully"),
            @ApiResponse(responseCode = "400", description = "The provided data is invalid"),
            @ApiResponse(responseCode = "404", description = "The identifier is unknown to the system"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    ResponseEntity<?> delete(final String articleId, final HttpServletRequest request);

}
