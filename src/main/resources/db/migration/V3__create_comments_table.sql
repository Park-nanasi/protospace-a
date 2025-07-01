CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    content TEXT NULL,
    user_id INT NOT NULL,
    prototype_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE
);
