package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.constraints.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Long> likes = new HashSet<>();
    private Long count = 0L;
}
