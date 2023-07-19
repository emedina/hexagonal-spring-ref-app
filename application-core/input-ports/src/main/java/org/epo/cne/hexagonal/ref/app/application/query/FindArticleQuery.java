package org.epo.cne.hexagonal.ref.app.application.query;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.hexagonal.ref.app.shared.validation.Validations;
import org.epo.cne.sharedkernel.query.Query;

import java.util.List;

/**
 * Query to encapsulate a request to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see Query
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FindArticleQuery implements Query {

    private final String id;

    public static Validation<Error, FindArticleQuery> validateThenCreate(final String id) {
        return Validations.validateText(id)
                .map(FindArticleQuery::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
