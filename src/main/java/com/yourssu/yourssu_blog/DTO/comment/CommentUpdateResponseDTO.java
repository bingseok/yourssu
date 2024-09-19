package com.yourssu.yourssu_blog.DTO.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateResponseDTO {

    private Long commentId;
    private String email;
    private String content;

    public CommentUpdateResponseDTO(Long commentId, String email, String content) {
        this.commentId = commentId;
        this.email = email;
        this.content = content;
    }
}
