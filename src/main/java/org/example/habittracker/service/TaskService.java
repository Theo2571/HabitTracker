package org.example.habittracker.service;

import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.model.Task;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.TaskRepository;
import org.example.habittracker.repository.UserRepository;
import org.example.habittracker.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TaskResponse> getTasks() {
        User user = getCurrentUser();
        return taskRepository.findAllByUser(user).stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .toList();
    }

    public TaskResponse createTask(String title) {
        User user = getCurrentUser();
        Task task = taskRepository.save(new Task(title, user));
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        );
    }

    public void toggleTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.toggleCompleted();
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
