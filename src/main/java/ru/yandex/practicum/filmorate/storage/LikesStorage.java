package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface LikesStorage {

    Set<Long> getLikes(Long filmId);
}
