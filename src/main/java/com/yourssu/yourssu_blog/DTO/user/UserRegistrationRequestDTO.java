package com.yourssu.yourssu_blog.DTO.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDTO {

    private String email;
    private String password;
    private String username;
}
