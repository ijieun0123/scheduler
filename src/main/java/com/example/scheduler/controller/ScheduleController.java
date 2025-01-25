package com.example.scheduler.controller;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 스케줄 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto){
        ScheduleResponseDto scheduleResponseDto = scheduleService.saveSchedule(dto);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.CREATED);
    }

    // 스케줄 전체 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findByUpdatedAtRangeAndWriter(
        @RequestParam("updatedAt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate updatedAt,
        @RequestParam("writer") String writer
    ){
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.findByUpdatedAtRangeAndWriter(updatedAt, writer);

        return ResponseEntity.ok(scheduleResponseDtos);
    }

    // 스케줄 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }
}
