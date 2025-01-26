package com.example.scheduler.repository;

import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UserResponseDto saveUser(User user){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("created_at", user.getCreatedAt());
        parameters.put("updated_at", user.getUpdatedAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setId(key.longValue());

        UserResponseDto userResponseDto = new UserResponseDto(key.longValue(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

        return userResponseDto;
    }

    public User findUserById(Long userId){
        String sql = "SELECT * FROM user WHERE id = ?";

        List<User> users = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            return new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        });

        if(users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return users.get(0);
    }
}
