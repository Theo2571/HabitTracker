package org.example.habittracker.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.habittracker.dto.UpdateUserProfileRequest;
import org.example.habittracker.dto.UserProfileResponse;
import org.example.habittracker.security.SecurityUtils;
import org.example.habittracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User profile management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public UserProfileResponse getProfile() {
        String username = SecurityUtils.getCurrentUsername();
        return userService.getCurrentUserProfile(username);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public UserProfileResponse updateProfile(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        String username = SecurityUtils.getCurrentUsername();
        return userService.updateProfile(username, request);
    }
}
