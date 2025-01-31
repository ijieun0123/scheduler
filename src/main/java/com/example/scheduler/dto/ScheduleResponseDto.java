package com.example.scheduler.dto;

import com.example.scheduler.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;
    private Long userId;
    private String todo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto user;

    public ScheduleResponseDto(Schedule schedule, UserResponseDto userResponseDto){
        this.id = schedule.getId();
        this.userId = schedule.getUserId();
        this.todo = schedule.getTodo();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
        this.user = userResponseDto;
    }

    public ScheduleResponseDto(long id, Long userId, String todo, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.todo = todo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
