package org.example.habittracker.dto;

import java.time.LocalDate;

public class TaskResponse {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDate date;

    public TaskResponse(Long id, String title, boolean completed, LocalDate date) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDate getDate() {
        return date;
    }
}
