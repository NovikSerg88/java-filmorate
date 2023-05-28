package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from users";
        log.info("Получен список пользователей");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users (login, name, email, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        log.info("Пользователь с идентификатором {} создан", id);
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select user_id from users where user_id = ?",
                user.getId());
        if (sqlRowSet.next()) {
            String sqlQuery = "update users set login = ?, name = ?, email = ?, birthday = ? where user_id = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getLogin()
                    , user.getName()
                    , user.getEmail()
                    , user.getBirthday()
                    , user.getId());
            log.info("Обновлен пользователь с идентификатором {}", user.getId());
            return user;
        } else throw new UserNotFoundException("Пользователь не найден");
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "select * from users where user_id = ?";
        log.info("Найден пользователь с идентификатором {}", id);
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("user_id");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, login, name, email, birthday);
    }
}
