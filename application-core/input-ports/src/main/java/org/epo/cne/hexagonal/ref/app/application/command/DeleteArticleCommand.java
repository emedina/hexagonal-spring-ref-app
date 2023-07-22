package org.epo.cne.hexagonal.ref.app.application.command;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.command.Command;

import java.util.List;

import static org.epo.cne.hexagonal.ref.app.shared.validation.Validations.validateText;

/**
 * Command to encapsulate a request to delete an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteArticleCommand implements Command {

    private final String id;

    public static Validation<Error, DeleteArticleCommand> validateThenCreate(final String id) {
        return validateText(id)
                .map(DeleteArticleCommand::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
