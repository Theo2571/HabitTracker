package org.example.habittracker.dto;

import java.time.LocalDate;

public class TaskResponse {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDate date;
    private String frequency;
    private String reminder;
    private Integer streak;

    public TaskResponse(Long id, String title, boolean completed, LocalDate date) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.date = date;
    }

    public TaskResponse(Long id, String title, boolean completed, LocalDate date,
                        String frequency, String reminder, Integer streak) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.date = date;
        this.frequency = frequency;
        this.reminder = reminder;
        this.streak = streak;
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

    public String getFrequency() {
        return frequency;
    }

    public String getReminder() {
        return reminder;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }
}
