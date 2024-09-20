package com.emedina.hexagonal.ref.app.api;

/**
 * API request types.
 *
 * @author Enrique Medina Montenegro
 */
sealed interface ApiRequest {

    record Article(String id, String authorId, String title, String content) implements ApiRequest {
    }

}
