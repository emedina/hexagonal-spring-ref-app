package com.emedina.hexagonal.ref.app.application.ports.in;

import com.emedina.hexagonal.ref.app.application.command.DeleteArticleCommand;
import com.emedina.sharedkernel.application.annotation.UseCase;
import com.emedina.sharedkernel.command.core.CommandHandler;

/**
 * Use case to delete an article.
 *
 * @author Enrique Medina Montenegro
 * @see UseCase
 */
@UseCase
public interface DeleteArticleUseCase extends CommandHandler<DeleteArticleCommand> {
}
