package com.yourssu.yourssu_blog.controller;

import com.yourssu.yourssu_blog.DTO.comment.CommentDeleteRequestDTO;
import com.yourssu.yourssu_blog.DTO.comment.CommentUpdateRequestDTO;
import com.yourssu.yourssu_blog.DTO.comment.CommentUpdateResponseDTO;
import com.yourssu.yourssu_blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /* feat#2 */
    // 1. 댓글 작성
    @PostMapping("/comments/{articleId}")
    public ResponseEntity<CommentUpdateResponseDTO> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO) {

        CommentUpdateResponseDTO responseDTO = commentService.createComment(articleId, commentUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 2. 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponseDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO) {

        CommentUpdateResponseDTO responseDTO = commentService.updateComment(commentId, commentUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 3. 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestBody CommentDeleteRequestDTO commentDeleteRequestDTO) {

        commentService.deleteComment(commentId, commentDeleteRequestDTO);
        return ResponseEntity.noContent().build();
    }
}
