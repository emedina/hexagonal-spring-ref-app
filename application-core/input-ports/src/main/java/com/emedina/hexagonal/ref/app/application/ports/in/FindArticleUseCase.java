package com.emedina.hexagonal.ref.app.application.ports.in;

import io.vavr.control.Either;
import com.emedina.hexagonal.ref.app.application.query.FindArticleQuery;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.query.core.QueryHandler;

/**
 * Use case to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface FindArticleUseCase
        extends QueryHandler<Either<Error, ArticleDTO>, FindArticleQuery> {
}
