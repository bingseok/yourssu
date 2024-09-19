package com.yourssu.yourssu_blog.error;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime time;
    private String status;
    private String message;
    private String requestURI;

    public ErrorResponse(LocalDateTime time, String status, String message, String requestURI) {
        this.time = time;
        this.status = status;
        this.message = message;
        this.requestURI = requestURI;
    }
}
