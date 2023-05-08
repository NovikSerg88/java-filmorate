package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {

    Long setId();

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);
}
