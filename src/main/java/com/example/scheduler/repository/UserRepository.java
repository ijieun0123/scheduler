package com.example.scheduler.repository;

import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    UserResponseDto saveUser(User user);

    User findUserById(Long userId);
}
