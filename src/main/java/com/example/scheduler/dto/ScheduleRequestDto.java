package com.example.scheduler.dto;

import jakarta.validation.Valid;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
public class ScheduleRequestDto {

    @Valid
    private UserRequestDto user;

    @NotNull(message = "Todo cannot be null")
    @Size(min = 2, max = 200, message = "Todo should be between 2 and 200 characters")
    private String todo;

    @NotNull(message = "Password cannot be null")
    private String password;

    private Long userId;
    private int pageNumber;
    private int pageSize;

}
