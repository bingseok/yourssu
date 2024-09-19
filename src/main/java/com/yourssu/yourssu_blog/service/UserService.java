package com.yourssu.yourssu_blog.service;

import com.yourssu.yourssu_blog.DTO.user.UserRegistrationRequestDTO;
import com.yourssu.yourssu_blog.DTO.user.UserRegistrationResponseDTO;
import com.yourssu.yourssu_blog.entity.User;
import com.yourssu.yourssu_blog.error.defineException.InvalidUserException;
import com.yourssu.yourssu_blog.error.defineException.UserAlreadyExistsException;
import com.yourssu.yourssu_blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 회원 가입
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("이미 가입된 회원입니다.");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUsername(registrationDto.getUsername());

        User newUser = userRepository.save(user);

        return new UserRegistrationResponseDTO(newUser.getEmail(), newUser.getUsername());
    }

    // 2. 회원 탈퇴
    @Transactional
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserException("이메일/비밀번호가 일치하지 않습니다.");
        }

        // 사용자 작성 게시글 및 댓글은 자동 삭제

        // 사용자 삭제
        userRepository.delete(user);
    }

}
