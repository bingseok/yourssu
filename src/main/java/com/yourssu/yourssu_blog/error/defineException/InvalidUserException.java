package com.yourssu.yourssu_blog.error.defineException;

public class InvalidUserException extends RuntimeException{
    public InvalidUserException(String message) {
        super(message);
    }
}
