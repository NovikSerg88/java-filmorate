package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank
    private String login;
    private String name;
    @Email
    private String email;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
