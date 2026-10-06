package com.example.lms.model;

public record UserSession(long id, String fullName, String email, Role role) {
    public enum Role { ADMIN, LIBRARIAN }
}
