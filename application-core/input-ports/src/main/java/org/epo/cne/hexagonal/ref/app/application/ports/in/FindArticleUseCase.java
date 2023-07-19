package org.epo.cne.hexagonal.ref.app.application.ports.in;

import io.vavr.control.Either;
import org.epo.cne.hexagonal.ref.app.application.query.FindArticleQuery;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.sharedkernel.application.annotation.UseCase;
import org.epo.cne.sharedkernel.query.core.QueryHandler;

/**
 * Use case to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see UseCase
 */
@UseCase
public interface FindArticleUseCase
        extends QueryHandler<Either<Error, ArticleDTO>, FindArticleQuery> {
}
