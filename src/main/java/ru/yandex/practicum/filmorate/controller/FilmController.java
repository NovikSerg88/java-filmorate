package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.InMemoryFilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
//    private final InMemoryFilmService inMemoryFilmService;
    private final FilmDbService filmDbService;

    @GetMapping
    public List<Film> getFilms() {
        return filmDbService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение фильма по id %d", id));
        return filmDbService.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Идет добавление фильма: " + film.getName());
        return filmDbService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Идет обновление фильма: " + film.getName());
        return filmDbService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@Valid @PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("Пользователь %d поставил лайк фильму %d", userId, id));
        return filmDbService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteFilm(@Valid @PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("Пользователь %d удалил лайк фильму %d", userId, id));
        return filmDbService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Valid
                                      @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info(String.format("Получить список из первых %d фильмов по количеству лайков", count));
        return filmDbService.getPopularFilms(count);
    }
}