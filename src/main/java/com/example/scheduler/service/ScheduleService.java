package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;

import java.time.LocalDate;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto findByUpdatedAtRangeAndWriter(LocalDate updatedAt, String writer);

}
