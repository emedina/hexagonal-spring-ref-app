package com.emedina.hexagonal.ref.app.application.ports.in;

import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.command.core.CommandHandler;

/**
 * Use case to create an article.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface CreateArticleUseCase extends CommandHandler<CreateArticleCommand> {
}
