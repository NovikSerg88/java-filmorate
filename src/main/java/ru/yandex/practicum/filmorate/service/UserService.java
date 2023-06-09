package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUserFriends(Set<Long> friendsId) {
        return userStorage.getListOfFriends(friendsId);
    }

    public List<User> getFriends(Long id) {
        Set<Long> friendsId = userStorage.getFriendsId(id);
        return getUserFriends(friendsId);
    }

    public void addFriend(Long id, Long friendId) {
        if (id == null || id <= 0) {
            throw new IncorrectParameterException("id");
        }
        if (friendId == null || friendId <= 0) {
            throw new IncorrectParameterException("friendId");
        }
        User requester = getUserById(id);
        if (requester.getFriends().contains(friendId)) {
            log.info("Пользователь {} уже в списке друзей", friendId);
        }
        requester.getFriends().add(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        userStorage.getFriendsId(id).remove(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        List<User> userFriends = getFriends(id);
        List<User> otherUserFriends = getFriends(otherId);
        for (User user : otherUserFriends) {
            if (userFriends.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }
}
