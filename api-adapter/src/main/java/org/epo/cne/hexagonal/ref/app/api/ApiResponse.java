package org.epo.cne.hexagonal.ref.app.api;

/**
 * API response types.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
sealed interface ApiResponse {

    record Article(String id, String author, String title, String content) implements ApiResponse {
    }

}
