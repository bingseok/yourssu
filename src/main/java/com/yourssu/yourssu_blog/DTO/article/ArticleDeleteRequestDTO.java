package com.yourssu.yourssu_blog.DTO.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDeleteRequestDTO {

    private String email;
    private String password;
}
