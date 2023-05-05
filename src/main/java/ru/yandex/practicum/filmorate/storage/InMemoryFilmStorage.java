package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Long setId() {
        return id++;
    }

    @Override
    public void addLike(Long id) {
        Long likes = films.get(id).getCount();
        likes++;
        films.get(id).setCount(likes);
    }

    @Override
    public void deleteLike(Long id) {
        Long likes = films.get(id).getCount();
        likes--;
        films.get(id).setCount(likes);
    }

    @Override
    public List<Film> getFilms() {
        List<Film> allFilms = new ArrayList<>();
        for (Map.Entry<Long, Film> film : films.entrySet()) {
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
            throw new FilmNotFoundException("Фильма с id " + film.getId() + " не существует.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильма с id " + id + " не существует.");
        }
        return films.get(id);
    }
}
