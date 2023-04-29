package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        for (Map.Entry<Integer, Film> film : films.entrySet()) {
            allFilms.add(film.getValue());
        }
        return allFilms;
    }

    private int setId() {
        return id++;
    }

    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм " + film.getName() +
                    " уже добавлен.");
        }
        film.setId(setId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id " + film.getId() + " не существует.");
        }
        films.put(film.getId(), film);
        return film;
    }
}
