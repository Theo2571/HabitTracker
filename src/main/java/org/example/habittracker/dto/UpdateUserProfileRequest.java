package org.example.habittracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Update user profile request")
public record UpdateUserProfileRequest(

        @Email
        @Schema(example = "new@mail.com", description = "New email")
        String email,

        @Size(max = 500)
        @Schema(example = "Updated bio text", description = "New bio")
        String bio

) {}
