package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateScheduleRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer", schedule.getWriter());
        parameters.put("todo", schedule.getTodo());
        parameters.put("password", schedule.getPassword());
        parameters.put("createdAt", schedule.getCreatedAt());
        parameters.put("updatedAt", schedule.getUpdatedAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(key.longValue(), schedule.getWriter(), schedule.getTodo(), schedule.getCreatedAt(), schedule.getUpdatedAt());

        return scheduleResponseDto;
    }

    @Override
    public List<Schedule> findByUpdatedAtRangeAndWriter(LocalDateTime startOfDay, LocalDateTime endOfDay, String writer) {
        String sql = "SELECT * FROM schedule WHERE updatedAt BETWEEN ? AND ? AND writer = ?";

        return jdbcTemplate.query(sql, new Object[]{startOfDay, endOfDay, writer}, (rs, rowNum) -> {
            return new Schedule(
                    rs.getLong("id"),
                    rs.getString("writer"),
                    rs.getString("todo"),
                    rs.getString("password"),
                    rs.getTimestamp("createdAt").toLocalDateTime(),
                    rs.getTimestamp("updatedAt").toLocalDateTime()
            );
        });

    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {
        String sql = "select * from schedule where id = ?";

        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapperV2(), id);

        return result.stream().findAny();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String sql = "select * from schedule where id = ?";

        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapperV2(), id);

        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id));
    }

    @Override
    public int updateSchedule(Long id, String password, String todo, String writer) {
        String sql = "update schedule SET todo = ?, writer = ?, updatedAt = NOW() where id = ?";

        int updateRow = jdbcTemplate.update(sql, todo, writer, id);

        return updateRow;
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException{
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("writer"),
                        rs.getString("todo"),
                        rs.getString("password"),
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        rs.getTimestamp("updatedAt").toLocalDateTime()
                );
            }
        };
    }
}
