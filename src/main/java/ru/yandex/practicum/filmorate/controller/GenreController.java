package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.db.GenreService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получение списка всех жанров");
        return genreService.getGenres();
    }

    @GetMapping("/{genre_id}")
    public Genre getGenreById(@PathVariable("genre_id") int id) {
        log.info("Получение жанра фильма по параметру {}", id);
        return genreService.getGenreById(id);
    }
}
