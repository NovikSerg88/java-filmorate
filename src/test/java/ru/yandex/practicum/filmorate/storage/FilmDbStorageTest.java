package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikesStorage likesStorage;

    @Test
    public void testFindFilmById() {
        Film validFilm1 = getValidFilm1();
        filmDbStorage.addFilm(validFilm1);

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilmById(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUnknownIdFilm() {
        Film validFilm = getValidFilm1();
        filmDbStorage.addFilm(validFilm);
        Film invalidFilm = new Film();
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> filmDbStorage.getFilmById(invalidFilm.getId())
        );
        assertEquals("Film not found", thrown.getMessage());
    }

    @Test
    public void testFindAllFilms() {
        Film validFilm1 = getValidFilm1();
        Film validFilm2 = getValidFilm2();
        filmDbStorage.addFilm(validFilm1);
        filmDbStorage.addFilm(validFilm2);

        List<Film> films = filmDbStorage.getFilms();
        for (Film film : films) {
            Long id = film.getId();
            AssertionsForInterfaceTypes
                    .assertThat(id)
                    .isEqualTo(film.getId());
        }
    }

    @Test
    public void testUpdateFilm() {
        Film validFilm1 = getValidFilm1();
        Film validFilm2 = getValidFilm2();
        validFilm2.setId(1L);
        filmDbStorage.addFilm(validFilm1);
        filmDbStorage.updateFilm(validFilm2);

        Film film = filmDbStorage.getFilmById(1L);
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Film2");
    }

    @Test
    public void testAddFilm() {
        Film validFilm1 = getValidFilm1();
        filmDbStorage.addFilm(validFilm1);

        Film film = filmDbStorage.getFilmById(1L);
        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Film1");
    }

    @Test
    public void testAddLike() {
        Film validFilm1 = filmDbStorage.addFilm(getValidFilm1());
        User validUser1 = userDbStorage.createUser(getValidUser1());

        filmDbStorage.addLike(validFilm1.getId(), validUser1.getId());
        Set<Long> filmLikes = likesStorage.getLikes(validFilm1.getId());

        AssertionsForInterfaceTypes
                .assertThat(filmLikes)
                .isNotEmpty()
                .contains(validUser1.getId());
    }

    @Test
    public void testDeleteLike() {
        Film validFilm1 = filmDbStorage.addFilm(getValidFilm1());
        User validUser1 = userDbStorage.createUser(getValidUser1());

        filmDbStorage.addLike(validFilm1.getId(), validUser1.getId());
        filmDbStorage.deleteLike(validFilm1.getId(), validUser1.getId());
        Set<Long> filmLikes = likesStorage.getLikes(validFilm1.getId());
        AssertionsForInterfaceTypes
                .assertThat(filmLikes)
                .isEmpty();
    }

    private Film getValidFilm1() {
        Film film = new Film();
        Rating mpa = new Rating();
        mpa.setId(1);
        film.setName("Film1");
        film.setDescription("FilmDescription1");
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDuration(100);
        film.setMpa(mpa);
        return film;
    }

    private Film getValidFilm2() {
        Film film = new Film();
        Rating mpa = new Rating();
        mpa.setId(2);
        film.setName("Film2");
        film.setDescription("FilmDescription2");
        film.setReleaseDate(LocalDate.of(2020, 12, 12));
        film.setDuration(200);
        film.setMpa(mpa);
        return film;
    }

    private User getValidUser1() {
        User user = new User();
        user.setLogin("user1Login");
        user.setName("user1Name");
        user.setEmail("user1@mail");
        user.setBirthday(LocalDate.of(1988, 12, 29));
        return user;
    }
}
