package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    private Rating ratingMapper(ResultSet rs) throws SQLException {
        Rating mpa = new Rating();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("rating_name"));
        return mpa;
    }

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT * FROM rating_MPA ORDER BY rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> ratingMapper(rs));
    }

    @Override
    public Rating getRatingById(int id) {
        String sql = "SELECT * FROM rating_MPA WHERE rating_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> ratingMapper(rs), id)
                .stream().findAny().orElseThrow(() -> new NotFoundException("Rating not found"));
    }
}
