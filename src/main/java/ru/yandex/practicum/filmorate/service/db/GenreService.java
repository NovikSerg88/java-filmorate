package ru.yandex.practicum.filmorate.service.db;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getFilmGenres(Long filmId) {
        return genreStorage.getFilmGenres(filmId);
    }

    public void addFilmGenre(Film film) {
        if (film.getGenres().isEmpty()) {
            genreStorage.deleteFilmGenres(film);
        } else genreStorage.addFilmGenre(film);
    }

    public void updateFilmGenre(Film film) {
        genreStorage.deleteFilmGenres(film);
        genreStorage.addFilmGenre(film);
    }
}
