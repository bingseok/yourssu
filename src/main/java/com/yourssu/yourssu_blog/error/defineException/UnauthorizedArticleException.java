package com.yourssu.yourssu_blog.error.defineException;

public class UnauthorizedArticleException extends RuntimeException {
    public UnauthorizedArticleException(String message) {
        super(message);
    }
}
