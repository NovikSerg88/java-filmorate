package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.queryForList(sql, Long.class, filmId);
        return new HashSet<>(likes);
    }
}
