package org.example.habittracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "User profile data")
public record UserProfileResponse(

        @Schema(example = "matvey", description = "Username")
        String username,

        @Schema(example = "test@mail.com", description = "User email")
        String email,

        @Schema(example = "Frontend & React developer", description = "User bio")
        String bio,

        @Schema(example = "2026-01-01T12:00:00", description = "Registration date")
        LocalDateTime createdAt

) {}
