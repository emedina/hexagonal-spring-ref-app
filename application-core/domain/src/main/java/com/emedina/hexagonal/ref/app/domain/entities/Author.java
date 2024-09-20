package com.emedina.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import static com.emedina.hexagonal.ref.app.shared.validation.Validations.validateMandatory;

/**
 * Represents an author in our Domain Model.
 *
 * @author Enrique Medina Montenegro
 */
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Author {

    @EqualsAndHashCode.Include
    private final AuthorId id;

    private final PersonName name;

    public static Validation<Error, Author> validateThenCreate(final AuthorId id, final PersonName name) {
        return Validation.combine(validateMandatory(id), validateMandatory(name))
                .ap((vid, vname) -> new Author((AuthorId) vid, (PersonName) vname))
                .mapError(e -> new Error.ValidationErrors(e.toJavaList()));
    }

}
