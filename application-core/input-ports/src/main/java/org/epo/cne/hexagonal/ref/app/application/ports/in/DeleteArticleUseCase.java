package org.epo.cne.hexagonal.ref.app.application.ports.in;

import org.epo.cne.hexagonal.ref.app.application.command.DeleteArticleCommand;
import org.epo.cne.sharedkernel.application.annotation.UseCase;
import org.epo.cne.sharedkernel.command.core.CommandHandler;

/**
 * Use case to delete an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 * @see UseCase
 */
@UseCase
public interface DeleteArticleUseCase extends CommandHandler<DeleteArticleCommand> {
}
