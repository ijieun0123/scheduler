package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto){

        Schedule schedule = new Schedule(dto.getWriter(), dto.getTodo(), dto.getPassword());

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public ScheduleResponseDto findByUpdatedAtRangeAndWriter(LocalDate updatedAt, String writer) {
        LocalDateTime startOfDay = updatedAt.atStartOfDay();
        LocalDateTime endOfDay = updatedAt.atTime(LocalTime.MAX);

        Schedule schedule = scheduleRepository.findByUpdatedAtRangeAndWriter(startOfDay, endOfDay, writer)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
                });

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getWriter(),
                schedule.getTodo(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
