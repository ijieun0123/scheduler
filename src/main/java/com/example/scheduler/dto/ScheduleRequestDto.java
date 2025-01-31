package com.example.scheduler.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    private UserRequestDto user;
    private String todo;
    private String password;
    private Long userId;
    private int pageNumber;
    private int pageSize;

}
