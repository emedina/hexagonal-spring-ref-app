package com.emedina.hexagonal.ref.app.application.ports.in;

import io.vavr.control.Either;
import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.query.core.QueryHandler;

import java.util.List;

/**
 * Use case to get all the available articles.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface GetAllArticlesUseCase
        extends QueryHandler<Either<Error, List<ArticleDTO>>, GetAllArticlesQuery> {
}
