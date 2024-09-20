package com.emedina.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.Validations;

import java.util.List;

/**
 * Represents the title of an article in our Domain Model.
 *
 * @author Enrique Medina Montenegro
 */
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {

    @EqualsAndHashCode.Include
    private final String value;

    public static Validation<Error, Title> validateThenCreate(final String title) {
        return Validations.validateText(title)
                .map(Title::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
