package com.yourssu.yourssu_blog.controller;

import com.yourssu.yourssu_blog.DTO.user.UserDeleteRequestDTO;
import com.yourssu.yourssu_blog.DTO.user.UserRegistrationRequestDTO;
import com.yourssu.yourssu_blog.DTO.user.UserRegistrationResponseDTO;
import com.yourssu.yourssu_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 회원 가입
    @PostMapping
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody UserRegistrationRequestDTO registrationDto) {

        UserRegistrationResponseDTO userRegistrationResponseDTO = userService.registerUser(registrationDto);
        return ResponseEntity.ok(userRegistrationResponseDTO);
    }

    // 2. 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteRequestDTO userDeleteRequestDTO) {
        userService.deleteUser(userDeleteRequestDTO.getEmail(), userDeleteRequestDTO.getPassword());
        return ResponseEntity.noContent().build();
    }
}
