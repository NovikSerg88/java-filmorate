package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение пользователя по id %d", id));
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(String.format("Запрос на добавление пользователя: %s", user.getLogin()));
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info(String.format("Запрос на обновление пользователя: %s", user.getLogin()));
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("Запрос на добавление пользователя %d в друзья к пользователю %d", friendId, id));
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (friendId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("Запрос на удаление пользователя %d из друзей пользователя %d", friendId, id));
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение списка друзей пользователя %d", id));
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        log.info(String.format("Запрос на получение списка общих друзей пользователей %d, %d", id, otherId));
        return userService.getCommonFriends(id, otherId);
    }
}
