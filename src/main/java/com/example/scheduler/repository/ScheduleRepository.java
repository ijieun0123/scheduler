package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<Schedule> findAll(int pageNum, int pageSize);

    List<Schedule> findByUpdatedAtRangeAndWriter(LocalDateTime startOfDay, LocalDateTime endOfDay, Long userId);

    Optional<Schedule> findScheduleById(Long id);

    Schedule findScheduleByIdOrElseThrow(Long id);

    int updateSchedule(Long id, String todo, User user);

    int deleteScheduleAndUser(Long id);
}
