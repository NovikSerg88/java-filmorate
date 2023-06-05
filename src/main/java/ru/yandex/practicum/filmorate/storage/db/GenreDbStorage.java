package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres ORDER BY genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new GenreMapper().mapRow(rs, rowNum));
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new GenreMapper().mapRow(rs, rowNum), id)
                .stream().findAny().orElseThrow(() -> new NotFoundException("Genre not found"));
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT g.genre_id, g.genre_name FROM genres g JOIN film_genres fg ON g.genre_id = fg.genre_id " +
                "JOIN films f ON fg.film_id = f.film_id WHERE f.FILM_ID =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new GenreMapper().mapRow(rs, rowNum), filmId);
    }

    @Override
    public void addFilmGenre(Film film) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        Set<Genre> genres = film.getGenres()
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public void deleteFilmGenres(Film film) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }
}
