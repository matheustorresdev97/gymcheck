package com.matheustorres.gympass.web.dtos.response;

import com.matheustorres.gympass.domain.models.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        Collection<String> roles,
        LocalDateTime createdAt
) {
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                user.getCreatedAt()
        );
    }
}
