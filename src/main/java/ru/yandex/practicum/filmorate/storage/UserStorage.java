package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Component
public interface UserStorage {

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    Set<Long> getFriendsId(Long id);

    List<User> getListOfFriends(Set<Long> friendsId);

}
