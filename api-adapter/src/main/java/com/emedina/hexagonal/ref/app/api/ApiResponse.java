package com.emedina.hexagonal.ref.app.api;

/**
 * API response types.
 *
 * @author Enrique Medina Montenegro
 */
sealed interface ApiResponse {

    record Article(String id, String author, String title, String content) implements ApiResponse {
    }

}
