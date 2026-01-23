package org.example.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CreateTaskRequest {

    @NotBlank(message = "Title must not be empty")
    private String title;

    private LocalDate date;

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
}
