package com.emedina.hexagonal.ref.app.shared.validation;

import java.util.List;

/**
 * Specific errors about validations.
 *
 * @author Enrique Medina Montenegro
 */
public sealed interface ValidationError {

    record CannotBeNull(Object obj) implements ValidationError {
    }

    record CannotBeEmpty(List<?> list) implements ValidationError {
    }

    record MustHaveContent(String name) implements ValidationError {
    }

    record Invalid(String value) implements ValidationError {
    }

}
