package org.epo.cne.hexagonal.ref.app.shared.dto;

import java.io.Serializable;

/**
 * Represents an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
public record ArticleDTO(String id, String title, String content, String author) implements Serializable {
}
