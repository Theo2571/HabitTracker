package org.example.habittracker.service;

import org.example.habittracker.dto.AuthResponse;
import org.example.habittracker.dto.LoginRequest;
import org.example.habittracker.dto.RegisterRequest;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.UserRepository;
import org.example.habittracker.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = new User(
                request.username,
                passwordEncoder.encode(request.password)
        );
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user.getUsername()));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(jwtService.generateToken(user.getUsername()));
    }
}
