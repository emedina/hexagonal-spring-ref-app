package org.epo.cne.hexagonal.ref.app.shared.error;

import org.epo.cne.hexagonal.ref.app.shared.validation.ValidationError;

import java.io.Serializable;
import java.util.List;

/**
 * Hierarchy of possible error values.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
public sealed interface Error extends Serializable {

    record ValidationErrors(List<ValidationError> errors) implements Error {
    }

    record MultipleErrors(List<Error> errors) implements Error {
    }

    sealed interface BusinessError extends Error {

        record UnknownArticle(String id) implements BusinessError {
        }

        record InvalidId(String id) implements BusinessError {
        }

    }

    sealed interface TechnicalError extends Error {

        record SomethingWentWrong(String message) implements TechnicalError {
        }

    }

}
