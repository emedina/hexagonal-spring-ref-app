package org.epo.cne.hexagonal.ref.app.api;

import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mappings for the API between DTOs and API responses.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Mapper
interface ApiMapper {

    ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

    ApiResponse.Article toArticleResponse(final ArticleDTO dto);

}
