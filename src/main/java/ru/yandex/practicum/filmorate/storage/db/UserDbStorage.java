package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;


@Component
@Qualifier("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserMapper().mapRow(rs, rowNum));
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERS WHERE USER_ID = ?",
                user.getId());
        if (sqlRowSet.next()) {
            String sql = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
            jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
            return user;
        } else throw new NotFoundException("User not found");
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user = jdbcTemplate.query(sql, (rs, rowNum) -> new UserMapper().mapRow(rs, rowNum), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("User not found"));
        Set<Long> friends = getFriendsId(id);
        user.setFriends(friends);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql = "INSERT INTO FRIENDSHIP (REQUESTER_ID, ADDRESSEE_ID, STATUS) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, false);
    }

    @Override
    public Set<Long> getFriendsId(Long id) {
        Set<Long> friendsId = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT ADDRESSEE_ID FROM FRIENDSHIP " +
                "WHERE REQUESTER_ID = ?", id);
        while (sqlRowSet.next()) {
            Long addresseeId = sqlRowSet.getLong("ADDRESSEE_ID");
            friendsId.add(addresseeId);
        }
        return friendsId;
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE REQUESTER_ID = ? AND ADDRESSEE_ID=?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getListOfFriends(Set<Long> friendsId) {
        String inSql = String.join(",", Collections.nCopies(friendsId.size(), "?"));
        List<User> users = jdbcTemplate.query(
                String.format("SELECT * FROM USERS WHERE USER_ID IN (%s)", inSql),
                new UserMapper(),
                friendsId.toArray());

        return users;
    }
}
