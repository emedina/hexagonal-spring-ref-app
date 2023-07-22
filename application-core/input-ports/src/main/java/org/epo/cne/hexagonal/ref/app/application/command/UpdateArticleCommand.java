package org.epo.cne.hexagonal.ref.app.application.command;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.command.Command;

import static org.epo.cne.hexagonal.ref.app.shared.validation.Validations.validateText;

/**
 * Command to encapsulate a request to create an article.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateArticleCommand implements Command {

    private final String id;
    private final String authorId;
    private final String title;
    private final String content;

    public static Validation<Error, UpdateArticleCommand> validateThenCreate(final String id, final String authorId,
                                                                             final String title, final String content) {
        return Validation.combine(validateText(id), validateText(authorId), validateText(title), validateText(content))
                .ap(UpdateArticleCommand::new)
                .mapError(e -> new Error.ValidationErrors(e.toJavaList()));
    }

}
