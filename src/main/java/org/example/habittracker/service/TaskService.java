package org.example.habittracker.service;

import org.example.habittracker.dto.CalendarDataResponse;
import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.model.Task;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.TaskRepository;
import org.example.habittracker.repository.UserRepository;
import org.example.habittracker.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                        task.isCompleted(),
                        task.getDate()
                ))
                .toList();
    }

    public TaskResponse createTask(String title, LocalDate date) {
        User user = getCurrentUser();
        LocalDate taskDate = date != null ? date : LocalDate.now();
        Task task = taskRepository.save(new Task(title, user, taskDate));
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getDate()
        );
    }

    public List<TaskResponse> getTasksByDate(LocalDate date) {
        User user = getCurrentUser();
        return taskRepository.findAllByUserAndDate(user, date).stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted(),
                        task.getDate()
                ))
                .toList();
    }

    public CalendarDataResponse getCalendarData(YearMonth yearMonth) {
        User user = getCurrentUser();
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<Task> tasks = taskRepository.findAllByUserAndDateBetween(user, startDate, endDate);
        
        Map<String, List<TaskResponse>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getDate().toString(),
                        Collectors.mapping(
                                task -> new TaskResponse(
                                        task.getId(),
                                        task.getTitle(),
                                        task.isCompleted(),
                                        task.getDate()
                                ),
                                Collectors.toList()
                        )
                ));
        
        return new CalendarDataResponse(tasksByDate);
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
