package com.yourssu.yourssu_blog.error.defineException;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message){
        super(message);
    }
}
