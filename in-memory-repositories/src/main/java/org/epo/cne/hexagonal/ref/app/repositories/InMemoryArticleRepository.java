package org.epo.cne.hexagonal.ref.app.repositories;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.domain.entities.Article;
import org.epo.cne.hexagonal.ref.app.domain.entities.ArticleId;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.Adapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link ArticleRepository} interface that uses an in-memory collection to store the articles.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Adapter
@RequiredArgsConstructor
class InMemoryArticleRepository implements ArticleRepository {

    final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();

    /**
     * Finds an article by its identifier.
     *
     * @param id the identifier of the article to find
     * @return either the article or an error
     */
    @Override
    public Either<Error, Article> findById(final ArticleId id) {
        return this.articles.containsKey(id)
                ? Either.right(this.articles.get(id))
                : Either.left(new Error.BusinessError.UnknownArticle(id.value()));
    }

}
