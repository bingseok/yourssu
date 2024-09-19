package com.yourssu.yourssu_blog.repository;

import com.yourssu.yourssu_blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
