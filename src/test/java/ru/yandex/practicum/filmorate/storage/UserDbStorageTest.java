package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    public void testFindUserById() {
        User validUser1 = getValidUser1();
        userDbStorage.createUser(validUser1);

        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindAllUsers() {
        User validUser1 = userDbStorage.createUser(getValidUser1());
        User validUser2 = userDbStorage.createUser(getValidUser2());

        List<User> users = userDbStorage.getUsers();
        assertThat(users)
                .contains(validUser1, validUser2);
    }

    @Test
    public void testUpdateUser() {
        User validUser1 = getValidUser1();
        User validUser2 = getValidUser2();
        validUser2.setId(1L);
        userDbStorage.createUser(validUser1);
        userDbStorage.updateUser(validUser2);

        User user = userDbStorage.getUserById(1L);
        assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "user2Login");
    }

    @Test
    public void testCreateUser() {
        User validUser1 = userDbStorage.createUser(getValidUser1());

        User user = userDbStorage.getUserById(1L);
        assertThat(user)
                .isNotNull()
                .isEqualTo(validUser1);
    }

    @Test
    public void testAddFriends() {
        User validUser1 = userDbStorage.createUser(getValidUser1());
        User validUser2 = userDbStorage.createUser(getValidUser2());

        userDbStorage.addFriends(validUser1.getId(), validUser2.getId());

        Set<Long> friends = userDbStorage.getFriends(validUser1.getId());
        assertThat(friends)
                .isNotEmpty()
                .contains(validUser2.getId());
    }

    @Test
    public void testGetFriends() {
        User validUser1 = userDbStorage.createUser(getValidUser1());
        User validUser2 = userDbStorage.createUser(getValidUser2());

        userDbStorage.addFriends(validUser1.getId(), validUser2.getId());

        Set<Long> friends = userDbStorage.getFriends(validUser1.getId());
        assertThat(friends)
                .isNotEmpty()
                .contains(validUser2.getId());
    }

    @Test
    public void testDeleteFromFriends() {
        User validUser1 = userDbStorage.createUser(getValidUser1());
        User validUser2 = userDbStorage.createUser(getValidUser2());

        userDbStorage.addFriends(validUser1.getId(), validUser2.getId());
        userDbStorage.deleteFromFriends(validUser1.getId(), validUser2.getId());
        Set<Long> friends = userDbStorage.getFriends(validUser1.getId());
        assertThat(friends)
                .isEmpty();
    }

    private User getValidUser1() {
        User user = new User();
        user.setLogin("user1Login");
        user.setName("user1Name");
        user.setEmail("user1@mail");
        user.setBirthday(LocalDate.of(1988, 12, 29));
        return user;
    }

    private User getValidUser2() {
        User user = new User();
        user.setLogin("user2Login");
        user.setName("user2Name");
        user.setEmail("user2@mail");
        user.setBirthday(LocalDate.of(1983, 2, 21));
        return user;
    }
}
