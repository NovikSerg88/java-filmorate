package ru.yandex.practicum.filmorate.service.db;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db.LikesDbStorage;

import java.util.Set;

@Service
public class LikeService {

    private final LikesDbStorage likesDbStorage;

    public LikeService(LikesDbStorage likesDbStorage) {
        this.likesDbStorage = likesDbStorage;
    }

    public Set<Long> getFilmLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
