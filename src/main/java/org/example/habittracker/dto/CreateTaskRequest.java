package org.example.habittracker.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTaskRequest {

    @NotBlank(message = "Title must not be empty")
    private String title;

    public CreateTaskRequest() {}

    public String getTitle() {
        return title;
    }
}
