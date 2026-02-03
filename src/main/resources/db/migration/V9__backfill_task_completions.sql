-- Backfill task_completions for tasks that are already marked completed
INSERT INTO task_completions (task_id, completion_date)
SELECT id, task_date FROM task WHERE completed = true
ON CONFLICT (task_id, completion_date) DO NOTHING;
