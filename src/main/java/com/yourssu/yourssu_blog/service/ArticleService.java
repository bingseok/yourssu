package com.yourssu.yourssu_blog.service;

import com.yourssu.yourssu_blog.DTO.article.ArticleRequestDTO;
import com.yourssu.yourssu_blog.DTO.article.ArticleDeleteRequestDTO;
import com.yourssu.yourssu_blog.DTO.article.ArticleResponseDTO;
import com.yourssu.yourssu_blog.entity.Article;
import com.yourssu.yourssu_blog.entity.User;
import com.yourssu.yourssu_blog.error.defineException.ArticleNotFoundException;
import com.yourssu.yourssu_blog.error.defineException.InvalidUserException;
import com.yourssu.yourssu_blog.error.defineException.UnauthorizedArticleException;
import com.yourssu.yourssu_blog.repository.ArticleRepository;
import com.yourssu.yourssu_blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 게시글 작성
    public ArticleResponseDTO createArticle(ArticleRequestDTO articleRequestDTO) {
        // 사용자 인증
        User user = userRepository.findByEmail(articleRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(articleRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // Article 생성 및 저장
        Article article = new Article();
        article.setTitle(articleRequestDTO.getTitle());
        article.setContent(articleRequestDTO.getContent());
        article.setUser(user);
        Article newArticle = articleRepository.save(article);

        return new ArticleResponseDTO(newArticle.getArticleId(), newArticle.getUser().getEmail(), newArticle.getTitle(), newArticle.getContent());
    }

    // 2. 게시글 수정
    @Transactional
    public ArticleResponseDTO updateArticle(Long articleId, ArticleRequestDTO articleRequestDTO) {
        // 사용자 인증 (이메일 및 비밀번호)
        User user = userRepository.findByEmail(articleRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(articleRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 게시글 존재 여부 확인
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));

        // 게시글 작성자가 맞는지 확인
        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedArticleException("본인의 게시글만 수정할 수 있습니다.");
        }

        // 게시글 수정
        article.setTitle(articleRequestDTO.getTitle());
        article.setContent(articleRequestDTO.getContent());

        Article updatedArticle = articleRepository.save(article);

        return new ArticleResponseDTO(updatedArticle.getArticleId(), updatedArticle.getUser().getEmail(), updatedArticle.getTitle(), updatedArticle.getContent());
    }

    // 3. 게시글 삭제
    @Transactional
    public void deleteArticle(Long articleId, ArticleDeleteRequestDTO articleDeleteRequestDTO) {
        // 사용자 인증
        User user = userRepository.findByEmail(articleDeleteRequestDTO.getEmail());
        if (user == null || !passwordEncoder.matches(articleDeleteRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));


        // 게시글 작성자 확인
        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedArticleException("본인의 게시글만 삭제할 수 있습니다.");
        }

        // 해당 게시글의 댓글은 자동으로 삭제
        // 게시글 삭제
        articleRepository.delete(article);
    }
}
