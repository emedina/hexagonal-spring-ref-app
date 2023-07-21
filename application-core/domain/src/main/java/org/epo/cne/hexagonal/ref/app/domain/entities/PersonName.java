package org.epo.cne.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.hexagonal.ref.app.shared.validation.Validations;

import java.util.List;

/**
 * Represents the name of a person who is the author of an article in our Domain Model.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonName {

    @EqualsAndHashCode.Include
    private final String value;

    public static Validation<Error, PersonName> validateThenCreate(final String name) {
        return Validations.validateText(name)
                .map(PersonName::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
