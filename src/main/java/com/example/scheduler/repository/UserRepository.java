package com.example.scheduler.repository;

import com.example.scheduler.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    Long saveUser(User user);

    User findUserById(Long userId);

    Long findUserIdByEmail(String email);

    void updateUser(User user);

    Long findUserIdByScheduleId(Long id);

    boolean existsByEmail(String email);
}
