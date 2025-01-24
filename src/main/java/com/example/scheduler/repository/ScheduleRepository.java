package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    Optional<Schedule> findByUpdatedAtRangeAndWriter(LocalDateTime startOfDay, LocalDateTime endOfDay, String writer);

}
