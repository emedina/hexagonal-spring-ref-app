package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.command.CreateArticleCommand;
import org.epo.cne.hexagonal.ref.app.application.command.DeleteArticleCommand;
import org.epo.cne.hexagonal.ref.app.application.ports.in.CreateArticleUseCase;
import org.epo.cne.hexagonal.ref.app.application.ports.in.DeleteArticleUseCase;
import org.epo.cne.hexagonal.ref.app.domain.entities.ArticleId;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;

/**
 * Orchestration logic for the use case to delete an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
final class DeleteArticleHandler implements DeleteArticleUseCase {

    private final ArticleRepository articleRepository;

    /**
     * Handles the command.
     *
     * @param command command to handle
     * @return an error if anything goes wrong
     */
    @Override
    public Either<Error, Void> handle(final DeleteArticleCommand command) {
        return ArticleId.validateThenCreate(command.id())
                .toEither()
                .flatMap(this.articleRepository::delete);
    }

}
