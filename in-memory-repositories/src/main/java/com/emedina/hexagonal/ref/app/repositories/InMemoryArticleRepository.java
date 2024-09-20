package com.emedina.hexagonal.ref.app.repositories;

import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.Adapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link ArticleRepository} interface that uses an in-memory collection to store the articles.
 *
 * @author Enrique Medina Montenegro
 */
@Adapter
@RequiredArgsConstructor
class InMemoryArticleRepository implements ArticleRepository {

    final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();

    /**
     * Gets all the articles.
     *
     * @return either the list of articles or an error
     */
    @Override
    public Either<Error, List<Article>> findAll() {
        return Try.of(() -> this.articles.values())
                .toEither()
                .<Error>mapLeft(t -> new Error.TechnicalError.SomethingWentWrong(t.getMessage()))
                .map(List::copyOf);
    }

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
