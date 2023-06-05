package ru.yandex.practicum.filmorate.service.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserDbService implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> listOfFriends = new ArrayList<>();
        Set<Long> friends = userStorage.getFriends(id);
        for (Long friendsId : friends) {
            listOfFriends.add(getUserById(friendsId));
        }
        return listOfFriends;
    }

    @Override
    public User addToFriends(Long id, Long friendId) {
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
        userStorage.addFriends(id, friendId);
        return requester;
    }

    @Override
    public User deleteFromFriends(Long id, Long friendId) {
        User requester = getUserById(id);
        User addressee = getUserById(friendId);
        userStorage.getFriends(id).remove(friendId);
        userStorage.deleteFromFriends(id, friendId);
        return requester;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = userStorage.getFriends(id);
        Set<Long> otherUserFriends = userStorage.getFriends(otherId);
        for (Long friendId : otherUserFriends) {
            if (userFriends.contains(friendId)) {
                commonFriends.add(getUserById(friendId));
            }
        }
        return commonFriends;
    }
}
