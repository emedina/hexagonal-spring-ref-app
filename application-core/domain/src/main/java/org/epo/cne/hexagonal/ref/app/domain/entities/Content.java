package org.epo.cne.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.hexagonal.ref.app.shared.validation.Validations;

import java.util.List;

/**
 * Represents the content of an article in our Domain Model.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Content {

    private final String value;

    public static Validation<Error, Content> validateThenCreate(final String content) {
        return Validations.validateText(content)
                .map(Content::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
