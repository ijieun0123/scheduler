package com.example.scheduler.dto;

import com.example.scheduler.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
