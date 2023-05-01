package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {

    int setId();

    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);
}
