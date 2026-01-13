package com.emedina.hexagonal.ref.app.application.query;

import java.util.List;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.Validations;
import com.emedina.sharedkernel.query.Query;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Query to encapsulate a request to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro
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
