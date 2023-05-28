--INSERT INTO users(login, name, email, birthday)
--VALUES ('BabaYaga', 'Jon', 'JonWick@yandex.ru', '2017-01-01'),
--('MrOlimpi', 'Arnold', 'IronArny@yandex.ru', '1960-01-01'),
--('Rocky', 'Silvester', 'Sly@yandex.ru', '1961-01-01');


INSERT INTO rating_mpa(rating_name)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

INSERT INTO genres(genre_name)
VALUES ('Comedy'), ('Drama'), ('Cartoon'), ('Thriller'), ('Documentary'), ('Action');

INSERT INTO films(name, description, release_date, duration, rating_id)
VALUES ('JonWick', 'Russian killer kils everyone on his way', '2017-01-01', 180, 1);