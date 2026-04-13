package com.matheustorres.gympass.web.dtos.response;

import com.matheustorres.gympass.domain.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record UserResponseDTO(
        @Schema(description = "ID único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "Nome do usuário", example = "John Doe")
        String name,

        @Schema(description = "E-mail do usuário", example = "john@example.com")
        String email,

        @Schema(description = "Lista de permissões (Roles)", example = "[\"ROLE_MEMBER\"]")
        Collection<String> roles,

        @Schema(description = "Data de criação da conta")
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
