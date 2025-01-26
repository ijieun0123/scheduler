package com.example.scheduler.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
