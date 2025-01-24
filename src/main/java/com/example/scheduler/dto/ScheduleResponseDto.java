package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;
    private String writer;
    private String todo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
