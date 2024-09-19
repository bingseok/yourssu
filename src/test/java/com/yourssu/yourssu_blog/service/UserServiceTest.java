package com.yourssu.yourssu_blog.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 회원가입성공() throws Exception {
        String userJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"username\": \"username\" }";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@yourssu.com"))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void 중복회원_가입실패() throws Exception {
        String userJson = "{ \"email\": \"user@yourssu.com\", \"password\": \"password\", \"username\": \"username\" }";

        // Register user the first time
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        // Try registering the same user again
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 가입된 회원입니다."));

    }
}