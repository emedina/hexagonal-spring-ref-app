package org.epo.cne.hexagonal.ref.app.api;

import lombok.RequiredArgsConstructor;

/**
 * Represents a response with the article data.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@RequiredArgsConstructor
class ArticleResponse {

    private final String id;
    private final String title;
    private final String content;
    private final String authorName;

}
