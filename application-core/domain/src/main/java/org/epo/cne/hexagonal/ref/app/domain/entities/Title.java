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
 * Represents the title of an article in our Domain Model.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {

    private final String value;

    public static Validation<Error, Title> validateThenCreate(final String title) {
        return Validations.validateText(title)
                .map(Title::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
