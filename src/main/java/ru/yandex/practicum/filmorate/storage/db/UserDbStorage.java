package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserMapper().mapRow(rs, rowNum));
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (login, name, email, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = ?",
                user.getId());
        if (sqlRowSet.next()) {
            String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE user_id = ?";
            jdbcTemplate.update(sql
                    , user.getLogin()
                    , user.getName()
                    , user.getEmail()
                    , user.getBirthday()
                    , user.getId());
            return user;
        } else throw new UserNotFoundException("Пользователь не найден");
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserMapper().mapRow(rs, rowNum), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public void addFriends(Long id, Long friendId) {
        String sql = "INSERT INTO friendship (requester_id, addressee_id, status) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, false);
    }

    @Override
    public Set<Long> getFriends(Long id) {
        Set<Long> friends = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT addressee_id FROM friendship " +
                "WHERE requester_id = ?", id);
        while (sqlRowSet.next()) {
            Long addressee_id = sqlRowSet.getLong("addressee_id");
            friends.add(addressee_id);
        }
        return friends;
    }

    @Override
    public User deleteFromFriends(Long id, Long friendId) {
        String sql = "DELETE FROM friendship WHERE requester_id=? AND addressee_id=?";
        jdbcTemplate.update(sql, id, friendId);
        return getUserById(id);
    }
}
