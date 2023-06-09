DROP TABLE IF EXISTS rating_MPA CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS rating_MPA (
rating_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_name varchar(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
genre_name varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
login varchar(255) NOT NULL,
name varchar(255),
email varchar(255),
birthday date
);

CREATE TABLE IF NOT EXISTS films (
film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(255) NOT NULL,
description varchar(255),
release_date date,
duration integer,
rating_id integer,
CONSTRAINT "film_rating_id" FOREIGN KEY (rating_id) REFERENCES rating_MPA
);

CREATE TABLE IF NOT EXISTS film_genres (
film_id integer,
genre_id integer,
CONSTRAINT "film_genres_films" FOREIGN KEY (film_id) REFERENCES films,
CONSTRAINT "film_genres_genres" FOREIGN KEY (genre_id) REFERENCES genres
);

CREATE TABLE IF NOT EXISTS friendship (
requester_id integer,
addressee_id integer,
status boolean,
CONSTRAINT "requester_users" FOREIGN KEY (requester_id) REFERENCES users,
CONSTRAINT "addressee_users" FOREIGN KEY (addressee_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS likes (
film_id integer,
user_id integer,
CONSTRAINT "likes_films" FOREIGN KEY (film_id) REFERENCES films,
CONSTRAINT "likes_users" FOREIGN KEY (user_id) REFERENCES users
);

