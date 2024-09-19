package com.yourssu.yourssu_blog.repository;

import com.yourssu.yourssu_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
