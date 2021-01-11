package com.example.rabittmqdemo.mapper;

import com.example.rabittmqdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMapper extends JpaRepository<User, String> {
}
