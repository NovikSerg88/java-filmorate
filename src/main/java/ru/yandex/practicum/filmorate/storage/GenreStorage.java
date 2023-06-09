package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenreById(int id);

    List<Genre> getGenres();

    List<Genre> getFilmGenres(Long filmId);

    void addFilmGenre(Film film);

    void deleteFilmGenres(Film film);
}
