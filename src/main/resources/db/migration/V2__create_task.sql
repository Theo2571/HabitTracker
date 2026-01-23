CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      completed BOOLEAN NOT NULL DEFAULT FALSE,
                      user_id BIGINT NOT NULL,
                      CONSTRAINT fk_task_user
                          FOREIGN KEY (user_id)
                              REFERENCES users(id)
                              ON DELETE CASCADE
);
