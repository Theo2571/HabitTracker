package org.example.habittracker.service;

import org.example.habittracker.dto.UpdateUserProfileRequest;
import org.example.habittracker.dto.UserProfileResponse;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getCurrentUserProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    public UserProfileResponse updateProfile(
            String username,
            UpdateUserProfileRequest request
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.bio() != null) {
            user.setBio(request.bio());
        }

        userRepository.save(user);

        return mapToResponse(user);
    }

    private UserProfileResponse mapToResponse(User user) {
        return new UserProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getCreatedAt()
        );
    }
}
