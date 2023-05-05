package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getFriends(Long id) {
        List<User> listOfFriends = new ArrayList<>();
        Set<Long> friends = userStorage.getUserById(id).getFriends();
        for (Long friendsId : friends) {
            listOfFriends.add(userStorage.getUserById(friendsId));
        }
        return listOfFriends;
    }

    public User addToFriends(Long id, Long friendId) {
        userStorage.updateUser(userStorage.getUserById(id)).getFriends().add(friendId);
        userStorage.updateUser(userStorage.getUserById(friendId)).getFriends().add(id);
        return userStorage.getUserById(id);
    }

    public User deleteFromFriends(Long id, Long friendId) {
        userStorage.updateUser(userStorage.getUserById(id)).getFriends().remove(friendId);
        return userStorage.getUserById(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = userStorage.getUserById(id).getFriends();
        Set<Long> otherUserFriends = userStorage.getUserById(otherId).getFriends();
        for (Long friendId : otherUserFriends) {
            if (userFriends.contains(friendId)) {
                commonFriends.add(userStorage.getUserById(friendId));
            }
        }
        return commonFriends;
    }
}


