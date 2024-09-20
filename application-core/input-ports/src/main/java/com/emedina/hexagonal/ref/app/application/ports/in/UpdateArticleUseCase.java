package com.emedina.hexagonal.ref.app.application.ports.in;

import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.command.core.CommandHandler;

/**
 * Use case to update an article.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface UpdateArticleUseCase extends CommandHandler<UpdateArticleCommand> {
}
