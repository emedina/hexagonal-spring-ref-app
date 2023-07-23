package org.epo.cne.hexagonal.ref.app.domain.repositories;

import io.vavr.control.Either;
import org.epo.cne.hexagonal.ref.app.domain.entities.Article;
import org.epo.cne.hexagonal.ref.app.domain.entities.ArticleId;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.domain.repository.annotation.Repository;

import java.util.List;

/**
 * A repository for articles.
 *
 * @autor Enrique Medina Montenegro (em54029)
 */
@Repository
public interface ArticleRepository {

    /**
     * Gets all the articles.
     */
    Either<Error, List<Article>> findAll();

    /**
     * Finds an article by its identifier.
     */
    Either<Error, Article> findById(final ArticleId id);

    /**
     * Saves an article.
     */
    Either<Error, Void> save(final Article article);

    /**
     * Updates an article.
     */
    Either<Error, Void> update(final Article article);

    /**
     * Deletes an article.
     */
    Either<Error, Void> delete(final ArticleId id);

}
