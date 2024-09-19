package com.yourssu.yourssu_blog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (주로 REST API에서 사용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users").permitAll()  // 회원 가입, 탈퇴
                        .requestMatchers("/api/articles/**").permitAll()
                        .requestMatchers("/api/articles/comments/**").permitAll()
                        .anyRequest().authenticated()  // 그 외 요청은 인증 필요
                );

        return http.build();
    }
}
