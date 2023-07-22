package org.epo.cne.hexagonal.ref.app.repositories;

import io.vavr.control.Either;
import io.vavr.control.Try;
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
        return Try.of(() -> this.articles.containsKey(id))
                .toEither()
                .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                .flatMap(exists -> exists ? Try.of(() -> this.articles.get(id)).toEither()
                        .mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                        : Either.left(new Error.BusinessError.UnknownArticle(id.value())));
    }

    /**
     * Saves an article.
     *
     * @param article the article to save
     * @return an error if the article could not be saved
     */
    @Override
    public Either<Error, Void> save(final Article article) {
        return Try.of(() -> this.articles.put(article.id(), article))
                .toEither()
                .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                .map(a -> null);
    }

    /**
     * Updates an article.
     *
     * @param article the article to update
     * @return an error if the article could not be updated
     */
    @Override
    public Either<Error, Void> update(final Article article) {
        return Try.of(() -> this.articles.containsKey(article.id()))
                .toEither()
                .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                .flatMap(exists -> exists ? Try.of(() -> this.articles.put(article.id(), article)).toEither()
                        .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                        .map(a -> null)
                        : Either.left(new Error.BusinessError.UnknownArticle(article.id().value())));
    }

    /**
     * Deletes an article
     *
     * @param id the article to delete
     * @return an error if the article could not be deleted
     */
    @Override
    public Either<Error, Void> delete(final ArticleId id) {
        return Try.of(() -> this.articles.containsKey(id))
                .toEither()
                .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                .flatMap(exists -> exists ? Try.of(() -> this.articles.remove(id)).toEither()
                        .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                        .map(a -> null)
                        : Either.left(new Error.BusinessError.UnknownArticle(id.value())));
    }

}
