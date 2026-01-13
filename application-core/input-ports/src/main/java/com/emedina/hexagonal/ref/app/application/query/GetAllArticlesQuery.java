package com.emedina.hexagonal.ref.app.application.query;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.query.Query;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Query to encapsulate a request to get all the available articles.
 *
 * @author Enrique Medina Montenegro
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
