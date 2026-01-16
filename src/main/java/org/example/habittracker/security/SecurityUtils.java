package org.example.habittracker.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUsername() {
        return (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
