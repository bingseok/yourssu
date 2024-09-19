package com.yourssu.yourssu_blog.DTO.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationResponseDTO {
    private String email;
    private String username;

    public UserRegistrationResponseDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
