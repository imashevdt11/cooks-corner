CREATE SEQUENCE image_id_seq;
CREATE TABLE images
(
    id   BIGINT PRIMARY KEY DEFAULT NEXTVAL('image_id_seq'),
    name VARCHAR(255) NOT NULL,
    url  TEXT         NOT NULL
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY DEFAULT NEXTVAL('user_id_seq'),
    image_id BIGINT REFERENCES images (id),
    username VARCHAR(50)  NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    bio      TEXT
);

CREATE SEQUENCE token_id_seq;
CREATE TABLE black_list
(
    id    BIGINT PRIMARY KEY DEFAULT NEXTVAL('token_id_seq'),
    token VARCHAR(255)
);

CREATE SEQUENCE recipe_id_seq;
CREATE TABLE recipes
(
    id               BIGINT PRIMARY KEY DEFAULT NEXTVAL('recipe_id_seq'),
    image_id         BIGINT REFERENCES images (id)                                NOT NULL,
    user_id          BIGINT REFERENCES users (id)                                 NOT NULL,
    name             VARCHAR(100)                                                 NOT NULL UNIQUE,
    description      TEXT                                                         NOT NULL,
    difficulty       VARCHAR(10) CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')) NOT NULL,
    category         VARCHAR(50) CHECK (category IN ('BREAKFAST', 'LUNCH', 'DINNER')),
    preparation_time VARCHAR(50)                                                  NOT NULL
);

CREATE SEQUENCE ingredient_id_seq;
CREATE TABLE ingredients
(
    id        BIGINT PRIMARY KEY DEFAULT NEXTVAL('ingredient_id_seq'),
    recipe_id BIGINT REFERENCES recipes (id) NOT NULL,
    name      VARCHAR(100)                   NOT NULL,
    amount    VARCHAR(100)                   NOT NULL,
    unit      VARCHAR(50) CHECK (unit IN ('SPOON', 'KG', 'GR', 'GRAND'))
);

CREATE SEQUENCE following_id_seq;
CREATE TABLE followings
(
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('following_id_seq'),
    follower_id BIGINT NOT NULL REFERENCES users (id),
    chef_id     BIGINT NOT NULL REFERENCES users (id)
);

CREATE SEQUENCE liked_recipes_id_seq;
CREATE TABLE liked_recipes
(
    id        BIGINT PRIMARY KEY DEFAULT NEXTVAL('liked_recipes_id_seq'),
    user_id   BIGINT NOT NULL REFERENCES users (id),
    recipe_id BIGINT NOT NULL REFERENCES recipes (id),
    is_liked  BOOLEAN DEFAULT TRUE
);

CREATE SEQUENCE saved_recipe_id_seq;
CREATE TABLE saved_recipes
(
    id        BIGINT PRIMARY KEY DEFAULT NEXTVAL('saved_recipe_id_seq'),
    user_id   BIGINT NOT NULL REFERENCES users (id),
    recipe_id BIGINT NOT NULL REFERENCES recipes (id),
    is_saved  BOOLEAN DEFAULT TRUE
);