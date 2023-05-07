package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(Long id, Long userId) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        filmStorage.addLike(id);
        filmStorage.getFilmById(id).getLikes().add(userId);
        return filmStorage.getFilmById(id);
    }

    public Film unlikeFilm(Long id, Long userId) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        filmStorage.deleteLike(id);
        filmStorage.getFilmById(id).getLikes().remove(userId);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilms().stream().sorted((p0, p1) -> p1.getCount().compareTo(p0.getCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
