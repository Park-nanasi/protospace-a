CREATE TABLE IF NOT EXISTS prototype_likes (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    prototype_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_like UNIQUE (user_id, prototype_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE
);
