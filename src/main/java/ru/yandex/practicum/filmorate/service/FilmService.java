package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film getFilmById(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film likeFilm(Long id, Long userId);

    Film unlikeFilm(Long id, Long userId);

    List<Film> getPopularFilms(Integer count);
}
