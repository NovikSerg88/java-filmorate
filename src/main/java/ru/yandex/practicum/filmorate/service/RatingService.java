package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class RatingService {

    private final RatingStorage ratingStorage;

    public List<Rating> getRatings() {
        return ratingStorage.getRatings();
    }

    public Rating getRatingById(int id) {
        return ratingStorage.getRatingById(id);
    }
}
