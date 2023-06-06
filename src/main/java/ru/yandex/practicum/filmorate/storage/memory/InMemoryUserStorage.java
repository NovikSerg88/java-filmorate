package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private Long id = 1L;
    private final Map<Long, User> users = new HashMap<>();

    public Long setId() {
        return id++;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с логином " + user.getLogin() +
                    " уже зарегистрирован.");
        }
        user.setId(setId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователя с id " + user.getId() + " не существует.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с id " + id + " не существует.");
        }
        return users.get(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
    }


    public Set<Long> getFriends(Long id) {
        return null;
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
    }

    @Override
    public Set<Long> getFriendsId(Long id) {
        return null;
    }

    @Override
    public List<User> getListOfFriends(Set<Long> friendsId) {
        return null;
    }

    public List<User> getFriends(Set<Long> friendsId) {
        return null;
    }
}



