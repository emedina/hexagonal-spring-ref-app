package com.emedina.hexagonal.ref.app.application;

import com.emedina.hexagonal.ref.app.application.command.DeleteArticleCommand;
import com.emedina.hexagonal.ref.app.application.ports.in.DeleteArticleUseCase;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.ApplicationService;
import com.emedina.sharedkernel.transactional.Transactional;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

/**
 * Orchestration logic for the use case to delete an article.
 *
 * @author Enrique Medina Montenegro
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
class DeleteArticleHandler implements DeleteArticleUseCase {

    private final ArticleRepository articleRepository;

    /**
     * Handles the command.
     *
     * @param command command to handle
     * @return an error if anything goes wrong
     */
    @Override
    @Transactional
    public Either<Error, Void> handle(final DeleteArticleCommand command) {
        return ArticleId.validateThenCreate(command.id())
            .toEither()
            .flatMap(this.articleRepository::delete);
    }

}
