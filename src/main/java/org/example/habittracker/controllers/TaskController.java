package org.example.habittracker.controllers;

import jakarta.validation.Valid;
import org.example.habittracker.dto.CalendarDataResponse;
import org.example.habittracker.dto.CreateTaskRequest;
import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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
        return service.createTask(request.getTitle(), request.getDate());
    }

    @PutMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        service.toggleTask(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
