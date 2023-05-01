package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public int setId() {
        return id++;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        for (Map.Entry<Integer, Film> film : films.entrySet()) {
            allFilms.add(film.getValue());
        }
        return allFilms;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм " + film.getName() +
                    " уже добавлен.");
        }
        film.setId(setId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id " + film.getId() + " не существует.");
        }
        films.put(film.getId(), film);
        return film;
    }
}
