package com.emedina.hexagonal.ref.app.application;

import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.ports.in.CreateArticleUseCase;
import com.emedina.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.ApplicationService;
import com.emedina.sharedkernel.transactional.Transactional;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

/**
 * Orchestration logic for the use case to create an article.
 *
 * @author Enrique Medina Montenegro
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
