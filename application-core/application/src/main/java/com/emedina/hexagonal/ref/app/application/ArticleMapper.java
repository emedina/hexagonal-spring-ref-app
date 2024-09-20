package com.emedina.hexagonal.ref.app.application;

import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.entities.Author;
import com.emedina.hexagonal.ref.app.domain.entities.AuthorId;
import com.emedina.hexagonal.ref.app.domain.entities.Content;
import com.emedina.hexagonal.ref.app.domain.entities.PersonName;
import com.emedina.hexagonal.ref.app.domain.entities.Title;
import io.vavr.control.Validation;
import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.hexagonal.ref.app.domain.entities.*;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper logic for the application layer.
 *
 * @author Enrique Medina Montenegro
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(target = "id", expression = "java(article.id().value())")
    @Mapping(target = "title", expression = "java(article.title().value())")
    @Mapping(target = "content", expression = "java(article.content().value())")
    @Mapping(target = "author", expression = "java(article.author().name().value())")
    ArticleDTO toArticleDto(final Article article);

    default Validation<Error, Author> toAuthor(final AuthorDTO author) {
        return Validation.combine(AuthorId.validateThenCreate(author.id()), PersonName.validateThenCreate(author.name()))
                .ap((aid, aname) -> Author.validateThenCreate(aid, aname))
                .map(Validation::get)
                .mapError(e -> new Error.MultipleErrors(e.toJavaList()));
    }

    default Validation<Error, Article> toArticle(final CreateArticleCommand command, final AuthorDTO author) {
        return toAuthor(author)
                .flatMap(a -> toArticle(command.id(), command.title(), command.content(), a));
    }

    default Validation<Error, Article> toArticle(final UpdateArticleCommand command, final AuthorDTO author) {
        return toAuthor(author)
                .flatMap(a -> toArticle(command.id(), command.title(), command.content(), a));
    }

    default Validation<Error, Article> toArticle(final String id, final String title, final String content, final Author author) {
        // Hardcoded author for the sake of simplicity.
        return Validation.combine(ArticleId.validateThenCreate(id),
                        Title.validateThenCreate(title),
                        Content.validateThenCreate(content))
                .ap((vid, vtitle, vcontent) -> Article.validateThenCreate(vid, vtitle, vcontent, author))
                .map(Validation::get)
                .mapError(e -> new Error.MultipleErrors(e.toJavaList()));
    }

}
