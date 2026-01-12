package com.emedina.hexagonal.ref.app.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emedina.hexagonal.ref.app.application.query.FindArticleQuery;
import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.entities.Author;
import com.emedina.hexagonal.ref.app.domain.entities.AuthorId;
import com.emedina.hexagonal.ref.app.domain.entities.Content;
import com.emedina.hexagonal.ref.app.domain.entities.PersonName;
import com.emedina.hexagonal.ref.app.domain.entities.Title;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for FindArticleHandler.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
class FindArticleHandlerTest {

    @Mock
    private ArticleRepository articleRepository;

    private FindArticleHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FindArticleHandler(articleRepository);
    }

    @Test
    void shouldFindArticle_whenValidQueryProvided() {
        // given
        FindArticleQuery query = FindArticleQuery.validateThenCreate("article-123").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-123").get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        ArticleDTO dto = result.get();
        assertThat(dto.id()).isEqualTo("article-123");
        assertThat(dto.title()).isEqualTo("Test Title");
        assertThat(dto.content()).isEqualTo("Test content");
        assertThat(dto.author()).isEqualTo("John Doe");
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldReturnError_whenArticleNotFound() {
        // given
        FindArticleQuery query = FindArticleQuery.validateThenCreate("article-not-found").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-not-found").get();
        Error notFoundError = new Error.MultipleErrors(java.util.List.of());

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.left(notFoundError));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(notFoundError);
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldReturnError_whenRepositoryFails() {
        // given
        FindArticleQuery query = FindArticleQuery.validateThenCreate("article-fail").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-fail").get();
        Error repositoryError = new Error.MultipleErrors(java.util.List.of());

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.left(repositoryError));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isLeft()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleUuidId_whenValidUuidProvided() {
        // given
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(uuidId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(uuidId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleSpecialCharactersInId_whenValidIdProvided() {
        // given
        String specialId = "article-123_test@domain.com";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(specialId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(specialId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleLongId_whenValidLongIdProvided() {
        // given
        String longId = "article-" + "a".repeat(100);
        FindArticleQuery query = FindArticleQuery.validateThenCreate(longId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(longId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleUnicodeId_whenValidUnicodeIdProvided() {
        // given
        String unicodeId = "article-测试-тест-🌍";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(unicodeId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(unicodeId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleNumericId_whenValidNumericIdProvided() {
        // given
        String numericId = "12345";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(numericId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(numericId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleSlugId_whenValidSlugProvided() {
        // given
        String slugId = "introduction-to-hexagonal-architecture";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(slugId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(slugId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleComplexArticle_whenComplexArticleFound() {
        // given
        FindArticleQuery query = FindArticleQuery.validateThenCreate("article-complex").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-complex").get();
        Article complexArticle = createComplexArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(complexArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        ArticleDTO dto = result.get();
        assertThat(dto.title()).contains("Advanced Hexagonal Architecture");
        assertThat(dto.content()).contains("# Introduction");
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleVersionedId_whenValidVersionedIdProvided() {
        // given
        String versionedId = "article-123-v2.1.0";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(versionedId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(versionedId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    @Test
    void shouldHandleUrlLikeId_whenValidUrlLikeIdProvided() {
        // given
        String urlLikeId = "https://example.com/articles/123";
        FindArticleQuery query = FindArticleQuery.validateThenCreate(urlLikeId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(urlLikeId).get();
        Article foundArticle = createValidArticle();

        when(articleRepository.findById(expectedArticleId)).thenReturn(Either.right(foundArticle));

        // when
        Either<Error, ArticleDTO> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).findById(expectedArticleId);
    }

    private Article createValidArticle() {
        ArticleId id = ArticleId.validateThenCreate("article-123").get();
        Title title = Title.validateThenCreate("Test Title").get();
        Content content = Content.validateThenCreate("Test content").get();
        Author author = createValidAuthor();
        return Article.validateThenCreate(id, title, content, author).get();
    }

    private Article createComplexArticle() {
        ArticleId id = ArticleId.validateThenCreate("article-complex").get();
        Title title = Title.validateThenCreate("Advanced Hexagonal Architecture: A Deep Dive").get();
        Content content = Content.validateThenCreate("# Introduction\n\nThis comprehensive article explores...").get();
        Author author = createValidAuthor();
        return Article.validateThenCreate(id, title, content, author).get();
    }

    private Author createValidAuthor() {
        AuthorId authorId = AuthorId.validateThenCreate("author-123").get();
        PersonName authorName = PersonName.validateThenCreate("John Doe").get();
        return Author.validateThenCreate(authorId, authorName).get();
    }

}
