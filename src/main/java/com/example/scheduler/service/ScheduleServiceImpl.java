package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto){

        Schedule schedule = new Schedule(dto.getWriter(), dto.getTodo(), dto.getPassword());

        ScheduleResponseDto scheduleResponseDto = scheduleRepository.saveSchedule(schedule);

        return scheduleResponseDto;
    }

    @Override
    public ScheduleResponseDto findByUpdatedAtRangeAndWriter(LocalDate updatedAt, String writer) {
        LocalDateTime startOfDay = updatedAt.atStartOfDay();
        LocalDateTime endOfDay = updatedAt.atTime(LocalTime.MAX);

        Schedule schedule = scheduleRepository.findByUpdatedAtRangeAndWriter(startOfDay, endOfDay, writer)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getWriter(),
                schedule.getTodo(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
