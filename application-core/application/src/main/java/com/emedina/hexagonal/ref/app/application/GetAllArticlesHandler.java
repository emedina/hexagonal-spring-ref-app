package com.emedina.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import com.emedina.hexagonal.ref.app.application.ports.in.GetAllArticlesUseCase;
import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.ApplicationService;
import com.emedina.sharedkernel.transactional.Transactional;

import java.util.List;

/**
 * Orchestration logic for the use case to find all the available articles.
 *
 * @author Enrique Medina Montenegro
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
    @Transactional(readOnly = true)
    public Either<Error, List<ArticleDTO>> handle(final GetAllArticlesQuery query) {
        return this.articleRepository.findAll()
                .map(la -> la.stream().map(ArticleMapper.INSTANCE::toArticleDto).toList());
    }

}
