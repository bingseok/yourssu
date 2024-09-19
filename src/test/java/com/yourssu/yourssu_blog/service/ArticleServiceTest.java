package com.yourssu.yourssu_blog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourssu.yourssu_blog.DTO.article.ArticleRequestDTO;
import com.yourssu.yourssu_blog.DTO.article.ArticleDeleteRequestDTO;
import com.yourssu.yourssu_blog.entity.Article;
import com.yourssu.yourssu_blog.entity.User;
import com.yourssu.yourssu_blog.repository.ArticleRepository;
import com.yourssu.yourssu_blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ArticleServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private String userJson;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        userJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"username\": \"username\" }";

        // user 등록
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        testUser = userRepository.findByEmail("user@yourssu.com");
        assertNotNull(testUser, "Test User가 만들어지지 않음");
    }

    @Test
    public void 게시글작성성공() throws Exception {
        ArticleRequestDTO articleRequestDTO = new ArticleRequestDTO();
        articleRequestDTO.setEmail("user@yourssu.com");
        articleRequestDTO.setPassword("password");
        articleRequestDTO.setTitle("title");
        articleRequestDTO.setContent("content");

        String articleJson = objectMapper.writeValueAsString(articleRequestDTO);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    public void 게시글작성실패_잘못된사용자() throws Exception {
        ArticleRequestDTO articleRequestDTO = new ArticleRequestDTO();
        articleRequestDTO.setEmail("wronguser@yourssu.com");
        articleRequestDTO.setPassword("wrongpassword");
        articleRequestDTO.setTitle("title");
        articleRequestDTO.setContent("content");

        String articleJson = objectMapper.writeValueAsString(articleRequestDTO);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 게시글수정성공() throws Exception {
        Article article = new Article();
        article.setContent("content");
        article.setTitle("title");
        article.setUser(testUser);
        Article savedArticle = articleRepository.save(article);

        String updateJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"title\": \"Updated title\", \"content\": \"Updated content\" }";

        mockMvc.perform(put("/api/articles/{articleId}", savedArticle.getArticleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@yourssu.com"))
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    public void 게시글수정실패_잘못된사용자() throws Exception {
        String articleId = "1"; // Assumes an article with ID 1 exists

        ArticleRequestDTO updateRequestDTO = new ArticleRequestDTO();
        updateRequestDTO.setEmail("wronguser@yourssu.com");
        updateRequestDTO.setPassword("wrongpassword");
        updateRequestDTO.setTitle("Updated Title");
        updateRequestDTO.setContent("Updated Content");

        String updateJson = objectMapper.writeValueAsString(updateRequestDTO);

        mockMvc.perform(put("/api/articles/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 게시글삭제성공() throws Exception {
        Article article = new Article();
        article.setContent("content");
        article.setTitle("title");
        article.setUser(testUser);
        Article savedArticle = articleRepository.save(article);

        String deleteJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"title\": \"Updated title\", \"content\": \"Updated content\" }";

        mockMvc.perform(delete("/api/articles/{articleId}", savedArticle.getArticleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isNoContent());
    }

    @Test
    public void 게시글삭제실패_잘못된사용자() throws Exception {
        String articleId = "1"; // Assumes an article with ID 1 exists

        ArticleDeleteRequestDTO deleteRequestDTO = new ArticleDeleteRequestDTO();
        deleteRequestDTO.setEmail("wronguser@yourssu.com");
        deleteRequestDTO.setPassword("wrongpassword");

        String deleteJson = objectMapper.writeValueAsString(deleteRequestDTO);

        mockMvc.perform(delete("/api/articles/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일/비밀번호가 일치하지 않습니다."));
    }
}
