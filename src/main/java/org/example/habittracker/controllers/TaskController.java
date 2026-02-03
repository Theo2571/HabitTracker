package org.example.habittracker.controllers;

import jakarta.validation.Valid;
import org.example.habittracker.dto.CalendarDataResponse;
import org.example.habittracker.dto.CreateTaskRequest;
import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return service.getTasks();
    }

    @GetMapping("/by-date")
    public List<TaskResponse> getTasksByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return service.getTasksByDate(date);
    }

    @GetMapping("/streaks")
    public Map<String, Map<Long, Integer>> getStreaks(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return Map.of("streaks", service.getStreaks(date));
    }

    @GetMapping("/calendar")
    public CalendarDataResponse getCalendarData(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        }
        return service.getCalendarData(yearMonth);
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return service.createTask(
                request.getTitle(),
                request.getDate(),
                request.getFrequency(),
                request.getReminder()
        );
    }

    /** BACKEND_JAVA.md: при валидном токене 200 OK и тело с обновлённой задачей. */
    @PutMapping("/{id}/toggle")
    public ResponseEntity<TaskResponse> toggle(@PathVariable Long id) {
        TaskResponse updated = service.toggleTaskAndGet(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
