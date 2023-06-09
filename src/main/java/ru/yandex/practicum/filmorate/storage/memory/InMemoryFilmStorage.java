package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    public Long setId() {
        return id++;
    }

    @Override
    public void addLike(Long id, Long userId) {
        films.get(id).getLikes().add(id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        films.get(id).getLikes().remove(userId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
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
