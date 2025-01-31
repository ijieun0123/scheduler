package com.example.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    @Setter
    private Long id;
    private Long userId;
    private String todo;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(Long userId, String todo, String password){
        this.userId = userId;
        this.todo = todo;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Schedule(long id, long userId, String todo, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.todo = todo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
