package com.yourssu.yourssu_blog.repository;

import com.yourssu.yourssu_blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
