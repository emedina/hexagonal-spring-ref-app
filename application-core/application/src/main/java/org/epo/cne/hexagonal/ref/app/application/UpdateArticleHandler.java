package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.command.CreateArticleCommand;
import org.epo.cne.hexagonal.ref.app.application.command.UpdateArticleCommand;
import org.epo.cne.hexagonal.ref.app.application.ports.in.UpdateArticleUseCase;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;
import org.epo.cne.sharedkernel.transactional.Transactional;

/**
 * Orchestration logic for the use case to update an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
final class UpdateArticleHandler implements UpdateArticleUseCase {

    private final ArticleRepository articleRepository;

    /**
     * Handles the command.
     *
     * @param command command to handle
     * @return an error if anything goes wrong
     */
    @Override
    @Transactional
    public Either<Error, Void> handle(final UpdateArticleCommand command) {
        return ArticleMapper.INSTANCE.toArticle(command)
                .toEither()
                .flatMap(this.articleRepository::update);
    }

}
