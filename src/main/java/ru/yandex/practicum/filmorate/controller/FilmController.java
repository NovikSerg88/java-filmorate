package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение фильма по id %d", id));
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Идет добавление фильма: " + film.getName());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Идет обновление фильма: " + film.getName());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@Valid @PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("Пользователь %d поставил лайк фильму %d", userId, id));
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteFilm(@Valid @PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("Пользователь %d удалил лайк фильму %d", userId, id));
        return filmService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Valid
                                      @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info(String.format("Получить список из первых %d фильмов по количеству лайков", count));
        return filmService.getPopularFilms(count);
    }
}