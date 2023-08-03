package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.command.CreateArticleCommand;
import org.epo.cne.hexagonal.ref.app.application.ports.in.CreateArticleUseCase;
import org.epo.cne.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import org.epo.cne.hexagonal.ref.app.domain.repositories.ArticleRepository;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.ApplicationService;
import org.epo.cne.sharedkernel.transactional.Transactional;

/**
 * Orchestration logic for the use case to create an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see ApplicationService
 */
@ApplicationService
@RequiredArgsConstructor
final class CreateArticleHandler implements CreateArticleUseCase {

    private final AuthorOutputPort authorOutputPort;
    private final ArticleRepository articleRepository;

    /**
     * Handles the command.
     *
     * @param command command to handle
     * @return an error if anything goes wrong
     */
    @Override
    @Transactional
    public Either<Error, Void> handle(final CreateArticleCommand command) {
        return this.authorOutputPort.lookupAuthor(command.authorId())
                .flatMap(author -> ArticleMapper.INSTANCE.toArticle(command, author).toEither())
                .flatMap(this.articleRepository::save);
    }

}
