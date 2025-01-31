package com.example.scheduler.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private String name;

    @Email(message = "Email should be valid")
    private String email;
}
