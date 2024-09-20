package com.emedina.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import com.emedina.hexagonal.ref.app.application.ports.in.FindArticleUseCase;
import com.emedina.hexagonal.ref.app.application.query.FindArticleQuery;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.ApplicationService;
import com.emedina.sharedkernel.transactional.Transactional;

/**
 * Orchestration logic for the use case to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
final class FindArticleHandler implements FindArticleUseCase {

    private final ArticleRepository articleRepository;

    /**
     * Handles the query.
     *
     * @param query query to handle
     * @return either the article or an error
     */
    @Override
    @Transactional(readOnly = true)
    public Either<Error, ArticleDTO> handle(final FindArticleQuery query) {
        return ArticleId.validateThenCreate(query.id())
                .toEither()
                .flatMap(this.articleRepository::findById)
                .map(ArticleMapper.INSTANCE::toArticleDto);
    }

}
