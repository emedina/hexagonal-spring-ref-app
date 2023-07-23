package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.ports.in.GetAllArticlesUseCase;
import org.epo.cne.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;

import java.util.List;

/**
 * Orchestration logic for the use case to find an article by its identifier.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
final class GetAllArticlesHandler implements GetAllArticlesUseCase {

    private final ArticleRepository articleRepository;

    /**
     * Handles the query.
     *
     * @param query query to handle
     * @return either the list of articles or an error
     */
    @Override
    public Either<Error, List<ArticleDTO>> handle(final GetAllArticlesQuery query) {
        return this.articleRepository.findAll()
                .map(la -> la.stream().map(ArticleMapper.INSTANCE::toArticleDto).toList());
    }

}
