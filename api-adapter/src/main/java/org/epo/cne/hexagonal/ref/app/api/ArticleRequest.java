package org.epo.cne.hexagonal.ref.app.api;

import lombok.RequiredArgsConstructor;

/**
 * Represents a request to create an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@RequiredArgsConstructor
class ArticleRequest {

    private final String title;
    private final String content;
    private final String authorId;

}
