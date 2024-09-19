package com.yourssu.yourssu_blog.DTO.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDTO {

    private Long articleId;
    private String email;
    private String title;
    private String content;

    public ArticleResponseDTO(Long articleId, String email, String title, String content) {
        this.articleId = articleId;
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
