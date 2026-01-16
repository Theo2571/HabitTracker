package org.example.habittracker.controllers;

import jakarta.validation.Valid;
import org.example.habittracker.dto.CreateTaskRequest;
import org.example.habittracker.dto.TaskResponse;
import org.example.habittracker.service.TaskService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return service.createTask(request.getTitle());
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
