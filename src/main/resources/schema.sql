DROP TABLE if exists role_permission_mapping;
DROP TABLE if exists permission;
DROP TABLE if exists account_role_mapping;
DROP TABLE if exists role;
DROP TABLE if exists account;

CREATE TABLE account
(
    id       integer GENERATED ALWAYS AS IDENTITY,
    phone    text,
    email    text,
    password text,
    PRIMARY KEY (id),
    UNIQUE NULLS NOT DISTINCT (phone),
    UNIQUE NULLS NOT DISTINCT (email)
);

CREATE TABLE role
(
    id   integer GENERATED ALWAYS AS IDENTITY,
    name text NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE account_role_mapping
(
    account_id integer NOT NULL,
    role_id    integer NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account,
    FOREIGN KEY (role_id) REFERENCES role
);

CREATE TABLE permission
(
    id   integer GENERATED ALWAYS AS IDENTITY,
    name text NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE role_permission_mapping
(
    role_id       integer NOT NULL,
    permission_id integer NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role,
    FOREIGN KEY (permission_id) REFERENCES permission
);

INSERT INTO account (phone, email, password)
VALUES ('13205007769', null, '$2a$10$hlsQmVPwTlsMhGxWMinP3uZk0EtVNb6i5uFkVUgvNPv36tI7jv1Pq');
