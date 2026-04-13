package com.matheustorres.gympass.domain.models.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
