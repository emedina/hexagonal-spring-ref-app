package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.ports.in.FindArticleUseCase;
import org.epo.cne.hexagonal.ref.app.application.query.FindArticleQuery;
import org.epo.cne.hexagonal.ref.app.domain.entities.ArticleId;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;
import org.epo.cne.sharedkernel.transactional.Transactional;

/**
 * Orchestration logic for the use case to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro (em54029)
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
