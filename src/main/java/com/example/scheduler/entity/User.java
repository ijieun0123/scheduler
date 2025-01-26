package com.example.scheduler.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class User {

    @Setter
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
