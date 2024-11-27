CREATE TABLE _user (
        id bigint generated by default as identity,
        email varchar(255) not null,
        password varchar(255) not null,
        role varchar(255) check (role in ('ADMIN','USER')),
        primary key (id)
);

CREATE TABLE task (
        id BIGINT PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        description VARCHAR(5000) NOT NULL,
        status VARCHAR(50) NOT NULL,
        task_priority VARCHAR(50) NOT NULL,
        author_id BIGINT NOT NULL,
        performer_id BIGINT NOT NULL,
        FOREIGN KEY (author_id) REFERENCES _user(id) ON DELETE CASCADE,
        FOREIGN KEY (performer_id) REFERENCES _user(id) ON DELETE CASCADE
);

CREATE TABLE comment (
        comment_id BIGINT PRIMARY KEY,
        task_id BIGINT NOT NULL,
        commentator_id BIGINT NOT NULL,
        content VARCHAR(2500) NOT NULL,
        timestamp TIMESTAMP NOT NULL,
        FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
        FOREIGN KEY (commentator_id) REFERENCES _user(id) ON DELETE CASCADE
);


CREATE TABLE _token (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        token VARCHAR(255) UNIQUE NOT NULL,
        token_type VARCHAR(20) NOT NULL CHECK (token_type IN ('BEARER')),
        revoked BOOLEAN NOT NULL,
        expired BOOLEAN NOT NULL,
        user_id BIGINT,
        FOREIGN KEY (user_id) REFERENCES _user(id) ON DELETE CASCADE
);