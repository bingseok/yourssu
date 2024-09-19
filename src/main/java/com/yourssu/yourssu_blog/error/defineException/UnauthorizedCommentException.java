package com.yourssu.yourssu_blog.error.defineException;

public class UnauthorizedCommentException extends RuntimeException{
    public UnauthorizedCommentException(String message){
        super(message);
    }
}
