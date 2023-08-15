package org.epo.cne.hexagonal.ref.app.api;

/**
 * API request types.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
sealed interface ApiRequest {

    record Article(String id, String authorId, String title, String content) implements ApiRequest {
    }

}
