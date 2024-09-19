package com.yourssu.yourssu_blog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourssu.yourssu_blog.DTO.article.ArticleRequestDTO;
import com.yourssu.yourssu_blog.DTO.comment.CommentDeleteRequestDTO;
import com.yourssu.yourssu_blog.entity.Article;
import com.yourssu.yourssu_blog.entity.Comment;
import com.yourssu.yourssu_blog.entity.User;
import com.yourssu.yourssu_blog.repository.ArticleRepository;
import com.yourssu.yourssu_blog.repository.CommentRepository;
import com.yourssu.yourssu_blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    private String userJson;
    private User testUser;
    private Article testArticle;
    private String articleId;

    @BeforeEach
    public void setup() throws Exception{
        userJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"username\": \"username\" }";

        // user 등록
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        testUser = userRepository.findByEmail("user@yourssu.com");
        assertNotNull(testUser, "Test User가 만들어지지 않음");

        // article 등록
        ArticleRequestDTO articleRequestDTO = new ArticleRequestDTO();
        articleRequestDTO.setEmail("user@yourssu.com");
        articleRequestDTO.setPassword("password");
        articleRequestDTO.setTitle("Initial Title");
        articleRequestDTO.setContent("Initial Content");

        String articleJson = objectMapper.writeValueAsString(articleRequestDTO);

        articleId = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .split(",")[0].split(":")[1].trim().replace("\"", "");

        testArticle = articleRepository.findById(Long.parseLong(articleId)).orElse(null);
        assertNotNull(testArticle, "Test article가 만들어지지 않음");
    }

    @Test
    public void 댓글작성성공() throws Exception {
        String commentJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"content\": \"comment\" }";

        mockMvc.perform(post("/api/articles/comments/{articleId}", testArticle.getArticleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@yourssu.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("comment"));
    }

    @Test
    public void 댓글작성실패_잘못된사용자() throws Exception {
        String commentJson = "{ \"email\": \"wronguser@yourssu.com\", \"password\": \"password\", \"content\": \"comment\" }";

        mockMvc.perform(post("/api/articles/comments/{articleId}", testArticle.getArticleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 댓글수정성공() throws Exception {
        Comment comment = new Comment();
        comment.setContent("Original content");
        comment.setUser(testUser);
        comment.setArticle(testArticle);
        Comment savedComment = commentRepository.save(comment);

        String updateJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"content\": \"Updated content\" }";

        mockMvc.perform(put("/api/articles/comments/{commentId}", savedComment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@yourssu.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Updated content"));
    }

    @Test
    public void 댓글수정실패_잘못된사용자() throws Exception {
        String commentId = "1"; // Assumes an article with ID 1 exists

        CommentDeleteRequestDTO deleteRequestDTO = new CommentDeleteRequestDTO();
        deleteRequestDTO.setEmail("wronguser@yourssu.com");
        deleteRequestDTO.setPassword("wrongpassword");

        String updateJson = objectMapper.writeValueAsString(deleteRequestDTO);

        mockMvc.perform(delete("/api/articles/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 댓글삭제성공() throws Exception {
        // Create a comment first
        Comment comment = new Comment();
        comment.setContent("Content");
        comment.setUser(testUser);
        comment.setArticle(testArticle);
        Comment savedComment = commentRepository.save(comment);

        String deleteJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\" }";

        mockMvc.perform(delete("/api/articles/comments/{commentId}", savedComment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void 댓글삭제실패_잘못된사용자() throws Exception {
        String commentId = "1"; // Assumes an article with ID 1 exists

        CommentDeleteRequestDTO deleteRequestDTO = new CommentDeleteRequestDTO();
        deleteRequestDTO.setEmail("wronguser@yourssu.com");
        deleteRequestDTO.setPassword("wrongpassword");

        String deleteJson = objectMapper.writeValueAsString(deleteRequestDTO);

        mockMvc.perform(delete("/api/articles/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }
}
