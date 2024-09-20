package com.emedina.hexagonal.ref.app.application.command;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.command.Command;

import static com.emedina.hexagonal.ref.app.shared.validation.Validations.validateText;

/**
 * Command to encapsulate a request to create an article.
 *
 * @author Enrique Medina Montenegro
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
