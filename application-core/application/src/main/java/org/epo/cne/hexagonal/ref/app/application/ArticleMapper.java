package org.epo.cne.hexagonal.ref.app.application;

import org.epo.cne.hexagonal.ref.app.domain.entities.Article;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
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
    ArticleDTO fromArticle(final Article article);

}
