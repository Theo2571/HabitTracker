package org.example.habittracker.service;

import org.example.habittracker.dto.CalendarDataResponse;
import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.model.Task;
import org.example.habittracker.model.TaskCompletion;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.TaskCompletionRepository;
import org.example.habittracker.repository.TaskRepository;
import org.example.habittracker.repository.UserRepository;
import org.example.habittracker.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCompletionRepository taskCompletionRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       TaskCompletionRepository taskCompletionRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCompletionRepository = taskCompletionRepository;
    }

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TaskResponse> getTasks() {
        User user = getCurrentUser();
        return taskRepository.findAllByUser(user).stream()
                .map(this::toTaskResponse)
                .toList();
    }

    public TaskResponse createTask(String title, LocalDate date, String frequency, String reminder) {
        User user = getCurrentUser();
        LocalDate taskDate = date != null ? date : LocalDate.now();
        Task task = new Task(title, user, taskDate);
        task.setFrequency(frequency);
        task.setReminder(reminder);
        task = taskRepository.save(task);
        return toTaskResponse(task);
    }

    public List<TaskResponse> getTasksByDate(LocalDate date) {
        User user = getCurrentUser();
        Map<Long, Integer> streaks = getStreaks(date);
        return taskRepository.findAllByUserAndDate(user, date).stream()
                .map(task -> {
                    TaskResponse r = toTaskResponse(task);
                    r.setStreak(streaks.getOrDefault(task.getId(), 0));
                    return r;
                })
                .toList();
    }

    private TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getDate(),
                task.getFrequency(),
                task.getReminder(),
                null
        );
    }

    public CalendarDataResponse getCalendarData(YearMonth yearMonth) {
        User user = getCurrentUser();
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<Task> tasks = taskRepository.findAllByUserAndDateBetween(user, startDate, endDate);
        
        Map<String, List<TaskResponse>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getDate().toString(),
                        Collectors.mapping(this::toTaskResponse, Collectors.toList())
                ));
        
        return new CalendarDataResponse(tasksByDate);
    }

    public void toggleTask(Long id) {
        toggleTaskInternal(id);
    }

    /** Toggle task and return updated task for API response. */
    public TaskResponse toggleTaskAndGet(Long id) {
        Task task = toggleTaskInternal(id);
        return toTaskResponse(task);
    }

    /**
     * Один и тот же обработчик для обоих направлений:
     * completed: false → true (поставить галочку) и completed: true → false (снять галочку).
     * Всегда возвращаем обновлённую задачу, проверка прав одна — по JWT, без учёта completed.
     */
    private Task toggleTaskInternal(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        LocalDate taskDate = task.getDate();
        task.toggleCompleted();
        taskRepository.save(task);
        if (task.isCompleted()) {
            taskCompletionRepository.findByTaskAndCompletionDate(task, taskDate)
                    .orElseGet(() -> taskCompletionRepository.save(new TaskCompletion(task, taskDate)));
        } else {
            // Снять галочку: удаляем запись из истории выполнений (find + delete, без лишних исключений)
            taskCompletionRepository.findByTaskAndCompletionDate(task, taskDate)
                    .ifPresent(taskCompletionRepository::delete);
        }
        return task;
    }

    /**
     * Returns map of taskId -> streak (consecutive days including given date).
     * Streak is computed by same habit title (completion history grouped by title).
     */
    public Map<Long, Integer> getStreaks(LocalDate date) {
        User user = getCurrentUser();
        List<Task> tasksOnDate = taskRepository.findAllByUserAndDate(user, date);
        Map<Long, Integer> result = new HashMap<>();
        for (Task task : tasksOnDate) {
            List<LocalDate> completionDates = taskCompletionRepository.findCompletionDatesByUserAndTitle(user, task.getTitle());
            int streak = computeStreak(completionDates, date);
            result.put(task.getId(), streak);
        }
        return result;
    }

    private int computeStreak(List<LocalDate> completionDates, LocalDate upToDate) {
        Set<LocalDate> set = new HashSet<>(completionDates);
        int count = 0;
        LocalDate d = upToDate;
        while (set.contains(d)) {
            count++;
            d = d.minusDays(1);
        }
        return count;
    }

    public void deleteTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
