package com.emedina.hexagonal.ref.app.shared.dto;

import java.io.Serializable;

/**
 * Represents an author.
 *
 * @author Enrique Medina Montenegro
 */
public record AuthorDTO(String id, String name) implements Serializable {
}
