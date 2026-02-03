CREATE TABLE task_completions (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    completion_date DATE NOT NULL,
    CONSTRAINT fk_task_completions_task
        FOREIGN KEY (task_id)
            REFERENCES task(id)
            ON DELETE CASCADE,
    CONSTRAINT uq_task_completion UNIQUE (task_id, completion_date)
);

CREATE INDEX idx_task_completions_task_id ON task_completions(task_id);
CREATE INDEX idx_task_completions_date ON task_completions(completion_date);
