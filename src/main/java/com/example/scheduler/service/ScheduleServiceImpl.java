package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.ScheduleRepository;
import com.example.scheduler.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
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
    private final UserRepository userRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto) {

        User user = new User(scheduleRequestDto.getUser().getName(), scheduleRequestDto.getUser().getEmail());

        UserResponseDto userResponseDto;

        try {
            userResponseDto = userRepository.saveUser(user);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Email already exists : " + scheduleRequestDto.getUser().getEmail());
        }

        Schedule schedule = new Schedule(user.getId(), scheduleRequestDto.getTodo(), scheduleRequestDto.getPassword());
        scheduleRepository.saveSchedule(schedule);

        return new ScheduleResponseDto(schedule, userResponseDto);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        return null;
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String password, String todo, Long writer) {
        return null;
    }

    @Override
    public List<ScheduleResponseDto> findByUpdatedAtRangeAndWriter(LocalDate updatedAt, Long userId) {
        LocalDateTime startOfDay = updatedAt.atStartOfDay();
        LocalDateTime endOfDay = updatedAt.atTime(LocalTime.MAX);

        logger.info("Fetching schedules for date range: {} to {} and userId: {}", startOfDay, endOfDay, userId);

        List<Schedule> schedules = scheduleRepository.findByUpdatedAtRangeAndWriter(startOfDay, endOfDay, userId);

        if(schedules.isEmpty()) {
            logger.warn("No schedule found for userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "schedules not found");
        }

        return schedules.stream()
                .sorted(Comparator.comparing(Schedule::getUpdatedAt).reversed())
                .map(schedule -> {
                    logger.info("Found schedule: {}", schedule);
                    // userId로 User 정보를 가져옴
                    User user = userRepository.findUserById(schedule.getUserId());

                    // UserResponseDto로 변환
                    UserResponseDto userResponseDto = new UserResponseDto(user);

                    logger.info("Created UserResponseDto: {}", userResponseDto);

                    // ScheduleResponseDto에 user 정보를 포함하여 반환
                    ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(
                            schedule.getId(),
                            schedule.getUserId(),
                            schedule.getTodo(),
                            schedule.getCreatedAt(),
                            schedule.getUpdatedAt(),
                            userResponseDto
                    );

                    logger.info("Created ScheduleResponseDto: {}", scheduleResponseDto);

                    logger.info("UserResponseDto details: id={}, name={}, email={}, createdAt={}, updatedAt={}",
                            userResponseDto.getId(),
                            userResponseDto.getName(),
                            userResponseDto.getEmail(),
                            userResponseDto.getCreatedAt(),
                            userResponseDto.getUpdatedAt()
                    );

                    return scheduleResponseDto;
                })
                .collect(Collectors.toList());
    }

//    @Override
//    public ScheduleResponseDto findScheduleById(Long id) {
//
//        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
//
//        return new ScheduleResponseDto(schedule);
//    }

//    @Override
//    public ScheduleResponseDto updateSchedule(Long id, String password, String todo, String writer) {
//
//        String storedPassword = scheduleRepository.findScheduleByIdOrElseThrow(id).getPassword();
//
//        if(!password.equals(storedPassword)){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The password is wrong.");
//        }
//
//        if(todo == null || writer == null){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The todo and writer are required values.");
//        }
//
//        int updateRow = scheduleRepository.updateSchedule(id, password, todo, writer);
//
//        if(updateRow == 0){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
//        }
//
//        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
//
//        return new ScheduleResponseDto(schedule);
//    }

    @Override
    public void deleteSchedule(Long id, String password) {

        String storedPassword = scheduleRepository.findScheduleByIdOrElseThrow(id).getPassword();

        if(!password.equals(storedPassword)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The password is wrong.");

        int deleteRow = scheduleRepository.deleteSchedule(id);

        if(deleteRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

    }

}
