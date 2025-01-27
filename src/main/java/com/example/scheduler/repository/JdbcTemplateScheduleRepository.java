package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
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
        parameters.put("user_id", schedule.getUserId());
        parameters.put("todo", schedule.getTodo());
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", schedule.getCreatedAt());
        parameters.put("updated_at", schedule.getUpdatedAt());

        System.out.println("Parameters: " + parameters);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setId(key.longValue());

        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(key.longValue(), schedule.getUserId(), schedule.getTodo(), schedule.getCreatedAt(), schedule.getUpdatedAt());

        return scheduleResponseDto;
    }

    @Override
    public List<Schedule> findByUpdatedAtRangeAndWriter(LocalDateTime startOfDay, LocalDateTime endOfDay, Long userId) {
        String sql = "SELECT * FROM schedule WHERE updated_at BETWEEN ? AND ? AND user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{startOfDay, endOfDay, userId}, (rs, rowNum) -> {
            return new Schedule(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("todo"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
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
    public int updateSchedule(Long id, String todo, User user) {
        String sql = "update schedule SET todo = ?, user_id = ?, updated_at = NOW() where id = ?";

        int updateRow = jdbcTemplate.update(sql, todo, user.getId(), id);

        return updateRow;
    }

    @Override
    public int deleteSchedule(Long id) {
        String sql = "delete from schedule where id = ?";

        int updateRow = jdbcTemplate.update(sql, id);

        return updateRow;
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException{
                return new Schedule(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("todo"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}
