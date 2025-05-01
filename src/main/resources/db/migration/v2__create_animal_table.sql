CREATE TABLE IF NOT EXISTS animals (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       type VARCHAR(100) NOT NULL,
                                       description TEXT,
                                       price DOUBLE PRECISION NOT NULL,
                                       image_url TEXT
);
