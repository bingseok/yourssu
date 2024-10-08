package com.yourssu.yourssu_blog.controller;

import com.yourssu.yourssu_blog.DTO.article.ArticleRequestDTO;
import com.yourssu.yourssu_blog.DTO.article.ArticleDeleteRequestDTO;
import com.yourssu.yourssu_blog.DTO.article.ArticleResponseDTO;
import com.yourssu.yourssu_blog.entity.Article;
import com.yourssu.yourssu_blog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;


    /* commit test*/
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 1. 게시글 작성
    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(@Valid @RequestBody ArticleRequestDTO articleRequestDTO) {
        ArticleResponseDTO articleDTO = articleService.createArticle(articleRequestDTO);
        return ResponseEntity.ok(articleDTO);
    }

    // 2. 게시글 수정
    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable Long articleId, @Valid @RequestBody ArticleRequestDTO articleRequestDTO) {

        ArticleResponseDTO articleResponseDTO = articleService.updateArticle(articleId, articleRequestDTO);
        return ResponseEntity.ok(articleResponseDTO);
    }

    // 3. 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId, @RequestBody ArticleDeleteRequestDTO articleDeleteRequestDTO) {
        articleService.deleteArticle(articleId, articleDeleteRequestDTO);
        return ResponseEntity.noContent().build();
    }
}
