package org.epo.cne.hexagonal.ref.app.domain.repositories;

import io.vavr.control.Either;
import org.epo.cne.hexagonal.ref.app.domain.entities.Article;
import org.epo.cne.hexagonal.ref.app.domain.entities.ArticleId;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.domain.repository.annotation.Repository;

/**
 * A repository for articles.
 *
 * @autor Enrique Medina Montenegro (em54029)
 */
@Repository
public interface ArticleRepository {

    /**
     * Finds an article by its identifier.
     */
    Either<Error, Article> findById(final ArticleId id);

}
