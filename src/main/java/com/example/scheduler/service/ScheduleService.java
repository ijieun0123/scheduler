package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> getSchedules(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findByUpdatedAtRangeAndWriter(LocalDate updatedAt, Long userId);

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto updateSchedule(Long id, String password, String todo, String name);

    void deleteSchedule(Long id, String password);
}
