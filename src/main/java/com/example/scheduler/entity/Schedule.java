package com.example.scheduler.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Schedule {

    private long id;
    private String writer;
    private String todo;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
