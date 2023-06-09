package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final LikeService likeService;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, GenreService genreService, LikeService likeService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.likeService = likeService;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(genreService.getFilmGenres(id));
        film.setLikes(likeService.getFilmLikes(id));
        return film;
    }

    public Film addFilm(Film film) {
        film = filmStorage.addFilm(film);
        genreService.addFilmGenre(film);
        film.setGenres(genreService.getFilmGenres(film.getId()));
        film.setLikes(likeService.getFilmLikes(film.getId()));
        return film;
    }

    public Film updateFilm(Film film) {
        film = filmStorage.updateFilm(film);
        genreService.updateFilmGenre(film);
        return getFilmById(film.getId());
    }

    public Film likeFilm(Long id, Long userId) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
        filmStorage.addLike(id, userId);
        return film;
    }

    public Film unlikeFilm(Long id, Long userId) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
        filmStorage.deleteLike(id, userId);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {

        return getFilms()
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}


