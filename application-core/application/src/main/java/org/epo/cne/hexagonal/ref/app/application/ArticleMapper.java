package org.epo.cne.hexagonal.ref.app.application;

import io.vavr.control.Validation;
import org.epo.cne.hexagonal.ref.app.application.command.CreateArticleCommand;
import org.epo.cne.hexagonal.ref.app.domain.entities.*;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper logic for the application layer.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(target = "id", expression = "java(article.id().value())")
    @Mapping(target = "title", expression = "java(article.title().value())")
    @Mapping(target = "content", expression = "java(article.content().value())")
    @Mapping(target = "author", expression = "java(article.author().name().value())")
    ArticleDTO toArticleDto(final Article article);

    default Validation<Error, Article> toArticle(final CreateArticleCommand command) {
        // Hardcoded author for the sake of simplicity.
        return Validation.combine(AuthorId.validateThenCreate(command.authorId()),
                        PersonName.validateThenCreate("William Shakespeare"))
                .ap(Author::validateThenCreate)
                .map(Validation::get)
                .map(author -> Validation.combine(ArticleId.validateThenCreate(command.id()),
                                Title.validateThenCreate(command.title()),
                                Content.validateThenCreate(command.content()))
                        .ap((id, title, content) -> Article.validateThenCreate(id, title, content, author))
                        .map(Validation::get))
                .map(Validation::get)
                .mapError(e -> new Error.MultipleErrors(e.toJavaList()));
    }

}
