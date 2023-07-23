package org.epo.cne.hexagonal.ref.app.application.query;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.query.Query;

/**
 * Query to encapsulate a request to get all the available articles.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see Query
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAllArticlesQuery implements Query {

    public static Validation<Error, GetAllArticlesQuery> validateThenCreate() {
        return Validation.valid(new GetAllArticlesQuery());
    }

}
