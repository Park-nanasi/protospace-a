CREATE TABLE IF NOT EXISTS prototypes (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    catchphrase VARCHAR(128) NOT NULL,
    concept VARCHAR(512) NOT NULL,
    image VARCHAR(256) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
