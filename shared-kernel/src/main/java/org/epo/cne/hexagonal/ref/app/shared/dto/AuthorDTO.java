package org.epo.cne.hexagonal.ref.app.shared.dto;

import java.io.Serializable;

/**
 * Represents an author.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
public record AuthorDTO(String id, String name) implements Serializable {
}
