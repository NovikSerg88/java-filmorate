package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db.LikesDbStorage;

import java.util.Set;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikesDbStorage likesDbStorage;

    public Set<Long> getFilmLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
