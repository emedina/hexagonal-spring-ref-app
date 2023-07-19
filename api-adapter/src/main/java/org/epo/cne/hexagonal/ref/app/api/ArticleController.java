package org.epo.cne.hexagonal.ref.app.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.epo.cne.hexagonal.ref.app.application.query.FindArticleQuery;
import org.epo.cne.hexagonal.ref.app.shared.dto.ArticleDTO;
import org.epo.cne.sharedkernel.command.core.CommandBus;
import org.epo.cne.sharedkernel.query.core.QueryBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Implementation of the API interface using a REST controller.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@RestController
@RequiredArgsConstructor
final class ArticleController implements ArticleApi {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    private final ApiErrorHandler apiErrorHandler;

    /**
     * @see ArticleApi#get(String, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> get(@PathVariable("articleId") final String articleId,
                                 final HttpServletRequest request) {
        return FindArticleQuery.validateThenCreate(articleId)
                .toEither()
                .<ArticleDTO>flatMap(this.queryBus::query)
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessResponse(HttpStatus.OK, ApiMapper.INSTANCE.toArticleResponse(a)));
    }

    /**
     * @see ArticleApi#create(ArticleRequest, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> create(@RequestBody final ArticleRequest articleRequest,
                                    final HttpServletRequest request) {
        return ResponseEntity.ok(null);
    }

}
