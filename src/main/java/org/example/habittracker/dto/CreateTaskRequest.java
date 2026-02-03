package org.example.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CreateTaskRequest {

    @NotBlank(message = "Title must not be empty")
    private String title;

    private LocalDate date;
    private String frequency;
    private String reminder;

    public CreateTaskRequest() {}

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
