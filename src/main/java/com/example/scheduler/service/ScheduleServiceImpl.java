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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ScheduleResponseDto> findByUpdatedAtRangeAndWriter(LocalDate updatedAt, String writer) {
        LocalDateTime startOfDay = updatedAt.atStartOfDay();
        LocalDateTime endOfDay = updatedAt.atTime(LocalTime.MAX);

        List<Schedule> schedules = scheduleRepository.findByUpdatedAtRangeAndWriter(startOfDay, endOfDay, writer);

        if(schedules.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "schedules not found");

        return schedules.stream()
                .sorted(Comparator.comparing(Schedule::getUpdatedAt).reversed())
                .map(schedule -> new ScheduleResponseDto(
                        schedule.getId(),
                        schedule.getWriter(),
                        schedule.getTodo(),
                        schedule.getCreatedAt(),
                        schedule.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String password, String todo, String writer) {

        String storedPassword = scheduleRepository.findScheduleByIdOrElseThrow(id).getPassword();

        if(!password.equals(storedPassword)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The password is wrong.");
        }

        if(todo == null || writer == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The todo and writer are required values.");
        }

        int updateRow = scheduleRepository.updateSchedule(id, password, todo, writer);

        if(updateRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

}
