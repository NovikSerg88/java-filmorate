package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(User user);

    List<User> getFriends(Long id);

    User addToFriends(Long id, Long friendId);

    User deleteFromFriends(Long id, Long friendId);

    List<User> getCommonFriends(Long id, Long otherId);
}
