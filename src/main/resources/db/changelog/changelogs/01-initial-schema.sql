CREATE TABLE movies(
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
duration_of_movie_seconds BIGINT,
rating DOUBLE PRECISION
);

CREATE TABLE users(
id BIGSERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL
);

CREATE TABLE watched_history(
id BIGSERIAL PRIMARY KEY,
user_id BIGINT,
movie_id BIGINT,
react VARCHAR(250),
when_watched DATE,
duration_of_movie_seconds BIGINT,
times_watched INTEGER,
status VARCHAR(250),

CONSTRAINT fk_history_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
CONSTRAINT fk_history_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);

CREATE TABLE user_preference(
id BIGSERIAL PRIMARY KEY,
user_id BIGINT,
types VARCHAR(255),
last_update DATE,
weight DOUBLE PRECISION,

CONSTRAINT fk_preference_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE movie_genres (
movie_id BIGINT NOT NULL,
genre VARCHAR(50) NOT NULL,
CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);

CREATE INDEX idx_movie_genres_movie_id ON movie_genres(movie_id);