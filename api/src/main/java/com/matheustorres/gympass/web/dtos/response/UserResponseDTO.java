package com.matheustorres.gympass.web.dtos.response;

import com.matheustorres.gympass.domain.models.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record UserResponseDTO(
        String id,
        String name,
        String email,
        Collection<String> roles,
        LocalDateTime createdAt
) {
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
