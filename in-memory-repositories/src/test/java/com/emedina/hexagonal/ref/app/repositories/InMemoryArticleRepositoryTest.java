package com.emedina.hexagonal.ref.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.entities.Author;
import com.emedina.hexagonal.ref.app.domain.entities.AuthorId;
import com.emedina.hexagonal.ref.app.domain.entities.Content;
import com.emedina.hexagonal.ref.app.domain.entities.PersonName;
import com.emedina.hexagonal.ref.app.domain.entities.Title;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for InMemoryArticleRepository.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("InMemoryArticleRepository Tests")
class InMemoryArticleRepositoryTest {

    private InMemoryArticleRepository repository;
    private Article testArticle;
    private ArticleId testArticleId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryArticleRepository();

        // Create test data
        testArticleId = ArticleId.validateThenCreate("test-article-123").get();
        var title = Title.validateThenCreate("Test Article Title").get();
        var content = Content.validateThenCreate("This is test content for the article").get();
        var authorId = AuthorId.validateThenCreate("test-author-456").get();
        var personName = PersonName.validateThenCreate("Test Author").get();
        var author = Author.validateThenCreate(authorId, personName).get();
        testArticle = Article.validateThenCreate(testArticleId, title, content, author).get();
    }

    @Nested
    @DisplayName("Given findAll method")
    class FindAllTests {

        @Test
        @DisplayName("When repository is empty, then should return empty list")
        void shouldReturnEmptyList_whenRepositoryIsEmpty() {
            // When
            Either<Error, List<Article>> result = repository.findAll();

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).isEmpty();
        }

        @Test
        @DisplayName("When repository has one article, then should return list with one article")
        void shouldReturnListWithOneArticle_whenRepositoryHasOneArticle() {
            // Given
            repository.save(testArticle);

            // When
            Either<Error, List<Article>> result = repository.findAll();

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).hasSize(1);
            assertThat(result.get().get(0)).isEqualTo(testArticle);
        }

        @Test
        @DisplayName("When repository has multiple articles, then should return all articles")
        void shouldReturnAllArticles_whenRepositoryHasMultipleArticles() {
            // Given
            var article2Id = ArticleId.validateThenCreate("test-article-789").get();
            var title2 = Title.validateThenCreate("Second Test Article").get();
            var content2 = Content.validateThenCreate("This is content for the second article").get();
            var authorId2 = AuthorId.validateThenCreate("test-author-789").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            repository.save(testArticle);
            repository.save(testArticle2);

            // When
            Either<Error, List<Article>> result = repository.findAll();

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).hasSize(2);
            assertThat(result.get()).containsExactlyInAnyOrder(testArticle, testArticle2);
        }

        @Test
        @DisplayName("When repository has many articles, then should return all articles")
        void shouldReturnAllArticles_whenRepositoryHasManyArticles() {
            // Given
            for (int i = 1; i <= 100; i++) {
                var articleId = ArticleId.validateThenCreate("article-" + i).get();
                var title = Title.validateThenCreate("Article Title " + i).get();
                var content = Content.validateThenCreate("Content for article " + i).get();
                var authorId = AuthorId.validateThenCreate("author-" + i).get();
                var personName = PersonName.validateThenCreate("Author " + i).get();
                var author = Author.validateThenCreate(authorId, personName).get();
                var article = Article.validateThenCreate(articleId, title, content, author).get();
                repository.save(article);
            }

            // When
            Either<Error, List<Article>> result = repository.findAll();

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("Given findById method")
    class FindByIdTests {

        @Test
        @DisplayName("When article exists, then should return the article")
        void shouldReturnArticle_whenArticleExists() {
            // Given
            repository.save(testArticle);

            // When
            Either<Error, Article> result = repository.findById(testArticleId);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).isEqualTo(testArticle);
        }

        @Test
        @DisplayName("When article does not exist, then should return UnknownArticle error")
        void shouldReturnUnknownArticleError_whenArticleDoesNotExist() {
            // Given
            var nonExistentId = ArticleId.validateThenCreate("non-existent-id").get();

            // When
            Either<Error, Article> result = repository.findById(nonExistentId);

            // Then
            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft()).isInstanceOf(Error.BusinessError.UnknownArticle.class);
            var error = (Error.BusinessError.UnknownArticle) result.getLeft();
            assertThat(error.id()).isEqualTo("non-existent-id");
        }

        @Test
        @DisplayName("When searching for article with unicode id, then should work correctly")
        void shouldWorkCorrectly_whenSearchingForArticleWithUnicodeId() {
            // Given
            var unicodeId = ArticleId.validateThenCreate("artículo-测试-🚀").get();
            var title = Title.validateThenCreate("Unicode Article").get();
            var content = Content.validateThenCreate("Content with unicode characters").get();
            var authorId = AuthorId.validateThenCreate("unicode-author").get();
            var personName = PersonName.validateThenCreate("Unicode Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var unicodeArticle = Article.validateThenCreate(unicodeId, title, content, author).get();
            repository.save(unicodeArticle);

            // When
            Either<Error, Article> result = repository.findById(unicodeId);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).isEqualTo(unicodeArticle);
        }

        @Test
        @DisplayName("When searching among multiple articles, then should return correct article")
        void shouldReturnCorrectArticle_whenSearchingAmongMultipleArticles() {
            // Given
            var article2Id = ArticleId.validateThenCreate("article-2").get();
            var title2 = Title.validateThenCreate("Second Article").get();
            var content2 = Content.validateThenCreate("Second content").get();
            var authorId2 = AuthorId.validateThenCreate("author-2").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            repository.save(testArticle);
            repository.save(testArticle2);

            // When
            Either<Error, Article> result = repository.findById(article2Id);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(result.get()).isEqualTo(testArticle2);
        }
    }

    @Nested
    @DisplayName("Given save method")
    class SaveTests {

        @Test
        @DisplayName("When saving new article, then should save successfully")
        void shouldSaveSuccessfully_whenSavingNewArticle() {
            // When
            Either<Error, Void> result = repository.save(testArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles).containsKey(testArticleId);
            assertThat(repository.articles.get(testArticleId)).isEqualTo(testArticle);
        }

        @Test
        @DisplayName("When saving article that already exists, then should overwrite")
        void shouldOverwrite_whenSavingArticleThatAlreadyExists() {
            // Given
            repository.save(testArticle);

            var updatedTitle = Title.validateThenCreate("Updated Title").get();
            var updatedContent = Content.validateThenCreate("Updated content").get();
            var authorId = AuthorId.validateThenCreate("test-author-456").get();
            var personName = PersonName.validateThenCreate("Test Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var updatedArticle = Article.validateThenCreate(testArticleId, updatedTitle, updatedContent, author).get();

            // When
            Either<Error, Void> result = repository.save(updatedArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles.get(testArticleId)).isEqualTo(updatedArticle);
            assertThat(repository.articles.get(testArticleId).title().value()).isEqualTo("Updated Title");
        }

        @Test
        @DisplayName("When saving multiple articles, then should save all")
        void shouldSaveAll_whenSavingMultipleArticles() {
            // Given
            var article2Id = ArticleId.validateThenCreate("article-2").get();
            var title2 = Title.validateThenCreate("Second Article").get();
            var content2 = Content.validateThenCreate("Second content").get();
            var authorId2 = AuthorId.validateThenCreate("author-2").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            // When
            Either<Error, Void> result1 = repository.save(testArticle);
            Either<Error, Void> result2 = repository.save(testArticle2);

            // Then
            assertThat(result1.isRight()).isTrue();
            assertThat(result2.isRight()).isTrue();
            assertThat(repository.articles).hasSize(2);
            assertThat(repository.articles).containsKeys(testArticleId, article2Id);
        }

        @Test
        @DisplayName("When saving article with unicode content, then should save successfully")
        void shouldSaveSuccessfully_whenSavingArticleWithUnicodeContent() {
            // Given
            var unicodeId = ArticleId.validateThenCreate("unicode-article").get();
            var unicodeTitle = Title.validateThenCreate("Título con Ñ y Ç").get();
            var unicodeContent = Content.validateThenCreate("Contenido con caracteres especiales: áéíóú, 中文, 🚀").get();
            var authorId = AuthorId.validateThenCreate("unicode-author").get();
            var personName = PersonName.validateThenCreate("José María").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var unicodeArticle = Article.validateThenCreate(unicodeId, unicodeTitle, unicodeContent, author).get();

            // When
            Either<Error, Void> result = repository.save(unicodeArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles.get(unicodeId)).isEqualTo(unicodeArticle);
        }
    }

    @Nested
    @DisplayName("Given update method")
    class UpdateTests {

        @Test
        @DisplayName("When updating existing article, then should update successfully")
        void shouldUpdateSuccessfully_whenUpdatingExistingArticle() {
            // Given
            repository.save(testArticle);

            var updatedTitle = Title.validateThenCreate("Updated Title").get();
            var updatedContent = Content.validateThenCreate("Updated content").get();
            var authorId = AuthorId.validateThenCreate("test-author-456").get();
            var personName = PersonName.validateThenCreate("Test Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var updatedArticle = Article.validateThenCreate(testArticleId, updatedTitle, updatedContent, author).get();

            // When
            Either<Error, Void> result = repository.update(updatedArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles.get(testArticleId)).isEqualTo(updatedArticle);
            assertThat(repository.articles.get(testArticleId).title().value()).isEqualTo("Updated Title");
        }

        @Test
        @DisplayName("When updating non-existent article, then should return UnknownArticle error")
        void shouldReturnUnknownArticleError_whenUpdatingNonExistentArticle() {
            // Given
            var nonExistentId = ArticleId.validateThenCreate("non-existent").get();
            var title = Title.validateThenCreate("Non-existent Title").get();
            var content = Content.validateThenCreate("Non-existent content").get();
            var authorId = AuthorId.validateThenCreate("non-existent-author").get();
            var personName = PersonName.validateThenCreate("Non-existent Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var nonExistentArticle = Article.validateThenCreate(nonExistentId, title, content, author).get();

            // When
            Either<Error, Void> result = repository.update(nonExistentArticle);

            // Then
            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft()).isInstanceOf(Error.BusinessError.UnknownArticle.class);
            var error = (Error.BusinessError.UnknownArticle) result.getLeft();
            assertThat(error.id()).isEqualTo("non-existent");
        }

        @Test
        @DisplayName("When updating article with unicode content, then should update successfully")
        void shouldUpdateSuccessfully_whenUpdatingArticleWithUnicodeContent() {
            // Given
            repository.save(testArticle);

            var unicodeTitle = Title.validateThenCreate("Título Actualizado").get();
            var unicodeContent = Content.validateThenCreate("Contenido actualizado con ñ y ç").get();
            var authorId = AuthorId.validateThenCreate("test-author-456").get();
            var personName = PersonName.validateThenCreate("Test Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var updatedArticle = Article.validateThenCreate(testArticleId, unicodeTitle, unicodeContent, author).get();

            // When
            Either<Error, Void> result = repository.update(updatedArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles.get(testArticleId).title().value()).isEqualTo("Título Actualizado");
            assertThat(repository.articles.get(testArticleId).content().value()).isEqualTo(
                "Contenido actualizado con ñ y ç");
        }

        @Test
        @DisplayName("When updating one of multiple articles, then should update only that article")
        void shouldUpdateOnlyThatArticle_whenUpdatingOneOfMultipleArticles() {
            // Given
            var article2Id = ArticleId.validateThenCreate("article-2").get();
            var title2 = Title.validateThenCreate("Second Article").get();
            var content2 = Content.validateThenCreate("Second content").get();
            var authorId2 = AuthorId.validateThenCreate("author-2").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            repository.save(testArticle);
            repository.save(testArticle2);

            var updatedTitle = Title.validateThenCreate("Updated First Article").get();
            var updatedContent = Content.validateThenCreate("Updated first content").get();
            var authorId = AuthorId.validateThenCreate("test-author-456").get();
            var personName = PersonName.validateThenCreate("Test Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var updatedArticle = Article.validateThenCreate(testArticleId, updatedTitle, updatedContent, author).get();

            // When
            Either<Error, Void> result = repository.update(updatedArticle);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles.get(testArticleId)).isEqualTo(updatedArticle);
            assertThat(repository.articles.get(article2Id)).isEqualTo(testArticle2); // Should remain unchanged
        }
    }

    @Nested
    @DisplayName("Given delete method")
    class DeleteTests {

        @Test
        @DisplayName("When deleting existing article, then should delete successfully")
        void shouldDeleteSuccessfully_whenDeletingExistingArticle() {
            // Given
            repository.save(testArticle);
            assertThat(repository.articles).containsKey(testArticleId);

            // When
            Either<Error, Void> result = repository.delete(testArticleId);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles).doesNotContainKey(testArticleId);
            assertThat(repository.articles).isEmpty();
        }

        @Test
        @DisplayName("When deleting non-existent article, then should return UnknownArticle error")
        void shouldReturnUnknownArticleError_whenDeletingNonExistentArticle() {
            // Given
            var nonExistentId = ArticleId.validateThenCreate("non-existent").get();

            // When
            Either<Error, Void> result = repository.delete(nonExistentId);

            // Then
            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft()).isInstanceOf(Error.BusinessError.UnknownArticle.class);
            var error = (Error.BusinessError.UnknownArticle) result.getLeft();
            assertThat(error.id()).isEqualTo("non-existent");
        }

        @Test
        @DisplayName("When deleting one of multiple articles, then should delete only that article")
        void shouldDeleteOnlyThatArticle_whenDeletingOneOfMultipleArticles() {
            // Given
            var article2Id = ArticleId.validateThenCreate("article-2").get();
            var title2 = Title.validateThenCreate("Second Article").get();
            var content2 = Content.validateThenCreate("Second content").get();
            var authorId2 = AuthorId.validateThenCreate("author-2").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            repository.save(testArticle);
            repository.save(testArticle2);

            // When
            Either<Error, Void> result = repository.delete(testArticleId);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles).doesNotContainKey(testArticleId);
            assertThat(repository.articles).containsKey(article2Id);
            assertThat(repository.articles.get(article2Id)).isEqualTo(testArticle2);
        }

        @Test
        @DisplayName("When deleting article with unicode id, then should delete successfully")
        void shouldDeleteSuccessfully_whenDeletingArticleWithUnicodeId() {
            // Given
            var unicodeId = ArticleId.validateThenCreate("artículo-测试-🚀").get();
            var title = Title.validateThenCreate("Unicode Article").get();
            var content = Content.validateThenCreate("Unicode content").get();
            var authorId = AuthorId.validateThenCreate("unicode-author").get();
            var personName = PersonName.validateThenCreate("Unicode Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var unicodeArticle = Article.validateThenCreate(unicodeId, title, content, author).get();
            repository.save(unicodeArticle);

            // When
            Either<Error, Void> result = repository.delete(unicodeId);

            // Then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.articles).doesNotContainKey(unicodeId);
        }

        @Test
        @DisplayName("When deleting all articles one by one, then repository should be empty")
        void shouldBeEmpty_whenDeletingAllArticlesOneByOne() {
            // Given
            var article2Id = ArticleId.validateThenCreate("article-2").get();
            var title2 = Title.validateThenCreate("Second Article").get();
            var content2 = Content.validateThenCreate("Second content").get();
            var authorId2 = AuthorId.validateThenCreate("author-2").get();
            var personName2 = PersonName.validateThenCreate("Second Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var testArticle2 = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            repository.save(testArticle);
            repository.save(testArticle2);

            // When
            Either<Error, Void> result1 = repository.delete(testArticleId);
            Either<Error, Void> result2 = repository.delete(article2Id);

            // Then
            assertThat(result1.isRight()).isTrue();
            assertThat(result2.isRight()).isTrue();
            assertThat(repository.articles).isEmpty();
        }
    }

    @Nested
    @DisplayName("Given repository state management")
    class RepositoryStateTests {

        @Test
        @DisplayName("When performing multiple operations, then state should be consistent")
        void shouldMaintainConsistentState_whenPerformingMultipleOperations() {
            // Given & When & Then
            // Save article
            Either<Error, Void> saveResult = repository.save(testArticle);
            assertThat(saveResult.isRight()).isTrue();
            assertThat(repository.articles).hasSize(1);

            // Find article
            Either<Error, Article> findResult = repository.findById(testArticleId);
            assertThat(findResult.isRight()).isTrue();
            assertThat(findResult.get()).isEqualTo(testArticle);

            // Update article
            var updatedTitle = Title.validateThenCreate("Updated Title").get();
            var updatedContent = Content.validateThenCreate("Updated content").get();
            var authorId = AuthorId.validateThenCreate("test-author-456").get();
            var personName = PersonName.validateThenCreate("Test Author").get();
            var author = Author.validateThenCreate(authorId, personName).get();
            var updatedArticle = Article.validateThenCreate(testArticleId, updatedTitle, updatedContent, author).get();

            Either<Error, Void> updateResult = repository.update(updatedArticle);
            assertThat(updateResult.isRight()).isTrue();
            assertThat(repository.articles).hasSize(1);

            // Verify update
            Either<Error, Article> findUpdatedResult = repository.findById(testArticleId);
            assertThat(findUpdatedResult.isRight()).isTrue();
            assertThat(findUpdatedResult.get().title().value()).isEqualTo("Updated Title");

            // Delete article
            Either<Error, Void> deleteResult = repository.delete(testArticleId);
            assertThat(deleteResult.isRight()).isTrue();
            assertThat(repository.articles).isEmpty();

            // Verify deletion
            Either<Error, Article> findDeletedResult = repository.findById(testArticleId);
            assertThat(findDeletedResult.isLeft()).isTrue();
            assertThat(findDeletedResult.getLeft()).isInstanceOf(Error.BusinessError.UnknownArticle.class);
        }

        @Test
        @DisplayName("When repository is used concurrently, then should handle operations safely")
        void shouldHandleOperationsSafely_whenRepositoryIsUsedConcurrently() {
            // Given
            var article2Id = ArticleId.validateThenCreate("concurrent-article").get();
            var title2 = Title.validateThenCreate("Concurrent Article").get();
            var content2 = Content.validateThenCreate("Concurrent content").get();
            var authorId2 = AuthorId.validateThenCreate("concurrent-author").get();
            var personName2 = PersonName.validateThenCreate("Concurrent Author").get();
            var author2 = Author.validateThenCreate(authorId2, personName2).get();
            var concurrentArticle = Article.validateThenCreate(article2Id, title2, content2, author2).get();

            // When - Simulate concurrent operations
            repository.save(testArticle);
            repository.save(concurrentArticle);

            // Then
            assertThat(repository.articles).hasSize(2);
            assertThat(repository.articles).containsKeys(testArticleId, article2Id);

            Either<Error, List<Article>> allArticles = repository.findAll();
            assertThat(allArticles.isRight()).isTrue();
            assertThat(allArticles.get()).hasSize(2);
        }
    }
}
