package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public List<Rating> getRatings() {
        log.info("Получение списка всех рейтингов");
        return ratingService.getRatings();
    }

    @GetMapping("/{rating_id}")
    public Rating getRatingById(@PathVariable("rating_id") int id) {
        log.info("Получение рейтинга по параметру {}", id);
        return ratingService.getRatingById(id);
    }
}
