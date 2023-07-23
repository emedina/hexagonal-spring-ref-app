package org.epo.cne.hexagonal.ref.app.application.ports.in;

import io.vavr.control.Either;
import org.epo.cne.hexagonal.ref.app.application.query.FindArticleQuery;
import org.epo.cne.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.sharedkernel.application.annotation.UseCase;
import org.epo.cne.sharedkernel.query.core.QueryHandler;

import java.util.List;

/**
 * Use case to get all the available articles.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see UseCase
 */
@UseCase
public interface GetAllArticlesUseCase
        extends QueryHandler<Either<Error, List<ArticleDTO>>, GetAllArticlesQuery> {
}
