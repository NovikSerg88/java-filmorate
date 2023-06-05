package ru.yandex.practicum.filmorate.service.db;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
public class RatingService {

    private final RatingStorage ratingStorage;

    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> getRatings() {
        return ratingStorage.getRatings();
    }

    public Rating getRatingById(int id) {
        return ratingStorage.getRatingById(id);
    }
}
