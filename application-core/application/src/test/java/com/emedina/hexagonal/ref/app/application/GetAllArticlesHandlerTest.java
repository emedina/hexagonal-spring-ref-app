package com.emedina.hexagonal.ref.app.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
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
 * Unit tests for GetAllArticlesHandler.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
class GetAllArticlesHandlerTest {

    @Mock
    private ArticleRepository articleRepository;

    private GetAllArticlesHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllArticlesHandler(articleRepository);
    }

    @Test
    void shouldGetAllArticles_whenValidQueryProvided() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> articles = Arrays.asList(
            createArticle("article-1", "First Article", "First content", "John Doe"),
            createArticle("article-2", "Second Article", "Second content", "Jane Smith"),
            createArticle("article-3", "Third Article", "Third content", "Bob Johnson")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(articles));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).hasSize(3);

        assertThat(dtos.get(0).id()).isEqualTo("article-1");
        assertThat(dtos.get(0).title()).isEqualTo("First Article");
        assertThat(dtos.get(0).content()).isEqualTo("First content");
        assertThat(dtos.get(0).author()).isEqualTo("John Doe");

        assertThat(dtos.get(1).id()).isEqualTo("article-2");
        assertThat(dtos.get(1).title()).isEqualTo("Second Article");

        assertThat(dtos.get(2).id()).isEqualTo("article-3");
        assertThat(dtos.get(2).title()).isEqualTo("Third Article");

        verify(articleRepository).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoArticlesExist() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> emptyList = Collections.emptyList();

        when(articleRepository.findAll()).thenReturn(Either.right(emptyList));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).isEmpty();
        verify(articleRepository).findAll();
    }

    @Test
    void shouldReturnError_whenRepositoryFails() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        Error repositoryError = new Error.MultipleErrors(java.util.List.of());

        when(articleRepository.findAll()).thenReturn(Either.left(repositoryError));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(repositoryError);
        verify(articleRepository).findAll();
    }

    @Test
    void shouldHandleSingleArticle_whenOnlyOneArticleExists() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> singleArticle = Arrays.asList(
            createArticle("article-single", "Single Article", "Single content", "Solo Author")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(singleArticle));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).id()).isEqualTo("article-single");
        assertThat(dtos.get(0).title()).isEqualTo("Single Article");
        verify(articleRepository).findAll();
    }

    @Test
    void shouldHandleLargeNumberOfArticles_whenManyArticlesExist() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> manyArticles = Arrays.asList(
            createArticle("article-1", "Article 1", "Content 1", "Author 1"),
            createArticle("article-2", "Article 2", "Content 2", "Author 2"),
            createArticle("article-3", "Article 3", "Content 3", "Author 3"),
            createArticle("article-4", "Article 4", "Content 4", "Author 4"),
            createArticle("article-5", "Article 5", "Content 5", "Author 5"),
            createArticle("article-6", "Article 6", "Content 6", "Author 6"),
            createArticle("article-7", "Article 7", "Content 7", "Author 7"),
            createArticle("article-8", "Article 8", "Content 8", "Author 8"),
            createArticle("article-9", "Article 9", "Content 9", "Author 9"),
            createArticle("article-10", "Article 10", "Content 10", "Author 10")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(manyArticles));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).hasSize(10);
        assertThat(dtos.get(0).id()).isEqualTo("article-1");
        assertThat(dtos.get(9).id()).isEqualTo("article-10");
        verify(articleRepository).findAll();
    }

    @Test
    void shouldHandleComplexArticles_whenComplexArticlesExist() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> complexArticles = Arrays.asList(
            createComplexArticle("article-complex-1", "Advanced Hexagonal Architecture",
                "# Introduction\n\nThis article explores advanced concepts...", "Dr. Architecture"),
            createComplexArticle("article-complex-2", "Microservices Design Patterns",
                "## Overview\n\nMicroservices patterns include:\n- Circuit Breaker\n- Saga Pattern", "Prof. Patterns")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(complexArticles));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).title()).contains("Advanced Hexagonal Architecture");
        assertThat(dtos.get(0).content()).contains("# Introduction");
        assertThat(dtos.get(1).title()).contains("Microservices Design Patterns");
        assertThat(dtos.get(1).content()).contains("## Overview");
        verify(articleRepository).findAll();
    }

    @Test
    void shouldHandleUnicodeContent_whenUnicodeArticlesExist() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> unicodeArticles = Arrays.asList(
            createArticle("article-unicode-1", "软件架构模式", "这篇文章介绍了软件架构的基本概念", "张三"),
            createArticle("article-unicode-2", "Архитектура ПО", "Эта статья объясняет принципы архитектуры",
                "Иван Петров"),
            createArticle("article-unicode-3", "Architecture 🏗️", "Building software with emojis 🚀",
                "Dev Master 👨‍💻")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(unicodeArticles));

        // when
        Either<Error, List<ArticleDTO>> result = handler.handle(query);

        // then
        assertThat(result.isRight()).isTrue();
        List<ArticleDTO> dtos = result.get();
        assertThat(dtos).hasSize(3);
        assertThat(dtos.get(0).title()).isEqualTo("软件架构模式");
        assertThat(dtos.get(1).title()).isEqualTo("Архитектура ПО");
        assertThat(dtos.get(2).title()).contains("🏗️");
        verify(articleRepository).findAll();
    }

    @Test
    void shouldHandleMultipleQueries_whenCalledSequentially() {
        // given
        GetAllArticlesQuery query1 = GetAllArticlesQuery.validateThenCreate().get();
        GetAllArticlesQuery query2 = GetAllArticlesQuery.validateThenCreate().get();
        List<Article> articles = Arrays.asList(
            createArticle("article-seq-1", "Sequential Article 1", "Content 1", "Author 1"),
            createArticle("article-seq-2", "Sequential Article 2", "Content 2", "Author 2")
        );

        when(articleRepository.findAll()).thenReturn(Either.right(articles));

        // when
        Either<Error, List<ArticleDTO>> result1 = handler.handle(query1);
        Either<Error, List<ArticleDTO>> result2 = handler.handle(query2);

        // then
        assertThat(result1.isRight()).isTrue();
        assertThat(result2.isRight()).isTrue();
        assertThat(result1.get()).hasSize(2);
        assertThat(result2.get()).hasSize(2);
        verify(articleRepository, org.mockito.Mockito.times(2)).findAll();
    }

    private Article createArticle(String id, String title, String content, String authorName) {
        ArticleId articleId = ArticleId.validateThenCreate(id).get();
        Title articleTitle = Title.validateThenCreate(title).get();
        Content articleContent = Content.validateThenCreate(content).get();
        Author author = createAuthor("author-" + id, authorName);
        return Article.validateThenCreate(articleId, articleTitle, articleContent, author).get();
    }

    private Article createComplexArticle(String id, String title, String content, String authorName) {
        ArticleId articleId = ArticleId.validateThenCreate(id).get();
        Title articleTitle = Title.validateThenCreate(title).get();
        Content articleContent = Content.validateThenCreate(content).get();
        Author author = createAuthor("author-" + id, authorName);
        return Article.validateThenCreate(articleId, articleTitle, articleContent, author).get();
    }

    private Author createAuthor(String id, String name) {
        AuthorId authorId = AuthorId.validateThenCreate(id).get();
        PersonName authorName = PersonName.validateThenCreate(name).get();
        return Author.validateThenCreate(authorId, authorName).get();
    }
}
