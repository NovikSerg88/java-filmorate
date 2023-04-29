package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        for (Map.Entry<Integer, User> user : users.entrySet()) {
            allUsers.add(user.getValue());
        }
        return allUsers;
    }

    private int setId() {
        return id++;
    }

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

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с id " + user.getId() + " не существует.");
        }
        users.put(user.getId(), user);
        return user;
    }
}
