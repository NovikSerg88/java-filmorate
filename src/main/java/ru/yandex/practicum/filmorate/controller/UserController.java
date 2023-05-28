package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    //    private final InMemoryUserService inMemoryUserService;
    private final UserDbService userDbService;

    @GetMapping
    public List<User> getUsers() {
        return userDbService.getUsers();
    }



    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение пользователя по id %d", id));
        return userDbService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(String.format("Запрос на добавление пользователя: %s", user.getLogin()));
        return userDbService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info(String.format("Запрос на обновление пользователя: %s", user.getLogin()));
        return userDbService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("Запрос на добавление пользователя %d в друзья к пользователю %d", friendId, id));
        return userDbService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("Запрос на удаление пользователя %d из друзей пользователя %d", friendId, id));
        return userDbService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long id) {
        log.info(String.format("Запрос на получение списка друзей пользователя %d", id));
        return userDbService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        log.info(String.format("Запрос на получение списка общих друзей пользователей %d, %d", id, otherId));
        return userDbService.getCommonFriends(id, otherId);
    }


}
