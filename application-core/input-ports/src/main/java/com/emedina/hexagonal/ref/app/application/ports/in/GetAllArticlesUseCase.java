package com.emedina.hexagonal.ref.app.application.ports.in;

import java.util.List;

import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.query.core.QueryHandler;

/**
 * Use case to get all the available articles.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface GetAllArticlesUseCase
        extends QueryHandler<Error, List<ArticleDTO>, GetAllArticlesQuery> {
}
