package com.driveease.rental.dto;

import lombok.Data;

public class AuthDTO {

    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private String role;
        private Long   userId;

        public AuthResponse(String token, String username, String role, Long userId) {
            this.token    = token;
            this.username = username;
            this.role     = role;
            this.userId   = userId;
        }
    }
}
