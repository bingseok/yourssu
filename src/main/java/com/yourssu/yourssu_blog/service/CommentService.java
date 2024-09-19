package com.yourssu.yourssu_blog.service;

import com.yourssu.yourssu_blog.DTO.comment.CommentDeleteRequestDTO;
import com.yourssu.yourssu_blog.DTO.comment.CommentUpdateRequestDTO;
import com.yourssu.yourssu_blog.DTO.comment.CommentUpdateResponseDTO;
import com.yourssu.yourssu_blog.entity.Article;
import com.yourssu.yourssu_blog.entity.Comment;
import com.yourssu.yourssu_blog.entity.User;
import com.yourssu.yourssu_blog.error.defineException.ArticleNotFoundException;
import com.yourssu.yourssu_blog.error.defineException.CommentNotFoundException;
import com.yourssu.yourssu_blog.error.defineException.InvalidUserException;
import com.yourssu.yourssu_blog.error.defineException.UnauthorizedCommentException;
import com.yourssu.yourssu_blog.repository.ArticleRepository;
import com.yourssu.yourssu_blog.repository.CommentRepository;
import com.yourssu.yourssu_blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 댓글 작성
    @Transactional
    public CommentUpdateResponseDTO createComment(Long articleId, CommentUpdateRequestDTO commentUpdateRequestDTO) {
        // 사용자 인증
        User user = userRepository.findByEmail(commentUpdateRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(commentUpdateRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 게시글 확인
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));

        // 댓글 생성
        Comment comment = new Comment();
        comment.setContent(commentUpdateRequestDTO.getContent());
        comment.setUser(user);
        comment.setArticle(article);

        Comment savedComment = commentRepository.save(comment);

        return new CommentUpdateResponseDTO(savedComment.getCommentId(), savedComment.getUser().getEmail(), savedComment.getContent());
    }

    // 2. 댓글 수정
    @Transactional
    public CommentUpdateResponseDTO updateComment(Long commentId, CommentUpdateRequestDTO commentUpdateRequestDTO) {

        // 사용자 인증
        User user = userRepository.findByEmail(commentUpdateRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(commentUpdateRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다."));

        // 댓글이 사용자건지 확인
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedCommentException("본인의 댓글만 수정할 수 있습니다.");
        }

        // 댓글 수정
        comment.setContent(commentUpdateRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return new CommentUpdateResponseDTO(updatedComment.getCommentId(), updatedComment.getUser().getEmail(), updatedComment.getContent());
    }

    // 3. 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, CommentDeleteRequestDTO commentDeleteRequestDTO) {
        // 사용자 인증
        User user = userRepository.findByEmail(commentDeleteRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(commentDeleteRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다."));

        // 댓글이 사용자건지 확인
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedCommentException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

}
