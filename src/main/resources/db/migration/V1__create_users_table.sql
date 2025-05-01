CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       email VARCHAR(255) UNIQUE,
                       phone_number VARCHAR(255),
                       password VARCHAR(255),
                       reset_password_token VARCHAR(255),
                       gender VARCHAR(50),
                       role VARCHAR(50)
);