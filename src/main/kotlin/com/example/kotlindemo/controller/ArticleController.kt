package com.example.kotlindemo.controller

import com.example.kotlindemo.model.Article
import com.example.kotlindemo.repository.ArticleRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// Sử dụng @RestController để đánh dấu đây là một controller
@RestController
// Sử dụng @RequestMapping để đánh dấu đường dẫn URL cho toàn bộ các phương thức trong controller này
@RequestMapping("/api")
class ArticleController(private val articleRepository: ArticleRepository){
    @GetMapping("/articles")
    fun getArticles(): List<Article> = articleRepository.findAll()

    @PostMapping("/articles")
    fun createNewArticle(@Valid @RequestBody article: Article): Article = articleRepository.save(article)

    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Article> {
        return articleRepository.findById(articleId).map { article ->
            ResponseEntity.ok(article)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/articles/{id}")
    fun updateArticleById(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {
        return articleRepository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle.copy(
                    title = newArticle.title,
                    content = newArticle.content,
            )
            ResponseEntity.ok().body(articleRepository.save(updatedArticle))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void>? {
        return articleRepository.findById(articleId).map { article ->
            articleRepository.delete(article)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}