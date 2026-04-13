package com.matheustorres.gympass.tests;

import com.matheustorres.gympass.domain.models.Role;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class UserFactory {

    public static User createUser() {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .name("John Doe")
                .email("john@example.com")
                .passwordHash("encrypted_password")
                .roles(Set.of(createRole()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Role createRole() {
        return new Role(1L, UserRole.MEMBER.getRole());
    }
}
