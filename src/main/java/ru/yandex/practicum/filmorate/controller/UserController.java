package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }


    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Идет добавление пользователя: " + user.getLogin());
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Идет обновление пользователя: " + user.getLogin());
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping("/users/{id}/friends")
    public Set<Long> getFriends(@PathVariable("id") Integer id) {
        return userService.getFriends(id);
    }
}
